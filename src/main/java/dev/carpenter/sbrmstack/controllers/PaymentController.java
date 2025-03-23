package dev.carpenter.sbrmstack.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import dev.carpenter.sbrmstack.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.stripe.model.Coupon;
import dev.carpenter.sbrmstack.security.services.StripeService;
import dev.carpenter.sbrmstack.utils.Response;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class PaymentController {

    @Autowired
    JwtUtils jwtUtils;

    @Value("${stripe.key.public}")
    private String API_PUBLIC_KEY;

    private StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @GetMapping("/")
    public String homepage() {
        return "homepage";
    }

    @PostMapping("/create-checkout-session")
    @ResponseBody
    public Response createCheckoutSession(@RequestBody Map<String, String> request) {
        String priceId = request.get("priceId");
        String successUrl = "http://localhost:3000/success";
        String cancelUrl = "http://localhost:3000/cancel";

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION) // Subscription mode
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(priceId)
                                    .setQuantity(1L)
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("sessionId", session.getId());

            return new Response(true, "Session created", response);
        } catch (StripeException e) {
            return new Response(false, "Stripe error: " + e.getMessage());
        }
    }

    @GetMapping("/subscription")
    public String subscriptionPage(Model model) {
        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
        return "subscription";
    }

    @GetMapping("/charge")
    public String chargePage(Model model) {
        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
        return "charge";
    }

    @PostMapping("/create-subscription")
    public @ResponseBody Response createSubscription(String email, String token, String plan, String coupon) {

        if (token == null || plan.isEmpty()) {
            return new Response(false, "Stripe payment token is missing. Please try again later.");
        }

        String customerId = stripeService.createCustomer(email, token);

        if (customerId == null) {
            return new Response(false, "An error accurred while trying to create customer");
        }

        String subscriptionId = stripeService.createSubscription(customerId, plan, coupon);

        if (subscriptionId == null) {
            return new Response(false, "An error accurred while trying to create subscription");
        }

        return new Response(true, "Success! your subscription id is " + subscriptionId);
    }

    @PostMapping("/cancel-subscription")
    public @ResponseBody Response cancelSubscription(String subscriptionId) {

        boolean subscriptionStatus = stripeService.cancelSubscription(subscriptionId);

        if (!subscriptionStatus) {
            return new Response(false, "Faild to cancel subscription. Please try again later");
        }

        return new Response(true, "Subscription cancelled successfully");
    }

    @PostMapping("/coupon-validator")
    public @ResponseBody Response couponValidator(String code) {

        Coupon coupon = stripeService.retriveCoupon(code);

        if (coupon != null && coupon.getValid()) {
            String details = (coupon.getPercentOff() == null ? "$" + (coupon.getAmountOff() / 100)
                    : coupon.getPercentOff() + "%") + "OFF" + coupon.getDuration();
            return new Response(true, details);
        }
        return new Response(false, "This coupon code is not available. This may be because it has expired or has "
                + "already been applied to your account.");
    }

    @PostMapping("/create-charge")
    public @ResponseBody Response createCharge(String email, String token) {

        if (token == null) {
            return new Response(false, "Stripe payment token is missing. please try again later.");
        }

        String chargeId = stripeService.createCharge(email, token, 999);//

        if (chargeId == null) {
            return new Response(false, "An error accurred while trying to charge.");
        }

        return new Response(true, "Success your charge id is " + chargeId);
    }
}