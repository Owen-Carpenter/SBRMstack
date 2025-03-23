import { useState } from "react";
import "../styles/Payment.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface PaymentProps {
    time: string;
    deal: string;
    rate: string;
    saving: string;
    tag: string;
    plan: string;  // Added plan type
}

function Payment({ time, deal, rate, saving, tag, plan }: PaymentProps) {
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handlePaymentClick = async () => {
        setLoading(true);

        try {
            // Check if the user is authenticated by verifying the JWT token
            const token = localStorage.getItem("jwtToken");
            if (!token) {
                alert("Please login to proceed with the payment.");
                navigate('/Login', { replace: true });
                return;
            }

            const result = await fetch("http://localhost:8080/create-subscription", {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ plan }),
                credentials: "include",  // Include cookies if necessary
                mode: "cors"  // Change mode to "cors" for proper CORS handling
            });

            const responseData = await result.json();  // Parse the JSON response

            if (responseData.url) {
                window.location.href = responseData.url;  // Redirect to Stripe session
            } else {
                alert("Something went wrong. Please try again.");
            }
        } catch (error) {
            console.error("Error creating checkout session:", error);
            alert("Error during payment processing. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="payment-card" onClick={handlePaymentClick}>
            {tag && <div className="tag">{tag}</div>}
            <div className="payment-left-side">
                <h1 className="payment-time">{time}</h1>
                <h2 className="payment-deal">{deal}</h2>
            </div>
            <div className="payment-right-side">
                <h1 className="payment-rate">{rate}</h1>
                <h3 className="payment-saving">{saving}</h3>
            </div>

            {loading && <div className="loading-spinner">Processing...</div>} {/* Loading spinner */}
        </div>
    );
}

export default Payment;
