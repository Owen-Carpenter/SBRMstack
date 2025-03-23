import "../styles/paywall.css";
import Payment from "../components/PaymentCard";
import Header from "../components/Header";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export function Paywall(){
    const nav = useNavigate();
    const isAuthenticated = !!localStorage.getItem("jwtToken");

    useEffect(() => {
        if (!isAuthenticated) {
            nav("/Login");
        }
    }, [isAuthenticated, nav]);

    return(
        <>
            <section className="paywall">
                <div className="content-container">
                    <Header />
                    <Payment plan="monthly" time="Monthly" deal="3 days free" rate="$0.99/month" saving="base" tag=""/>
                    <Payment plan="yearly" time="Yearly" deal="1 week free" rate="$9.99/year" saving="$0.83/month" tag="Save 15%" />
                    <Payment plan="lifetime" time="Lifetime" deal="forever..." rate="$24.99" saving="one-time" tag="Best Deal" />
                </div>
            </section>
        </>
    )
}