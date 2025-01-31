package com.Job_Prescreening_system.demo.Services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//subscription stripe trial
@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public StripeService() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createSubscription(String customerId, String priceId) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        params.put("items", List.of(Map.of("price", priceId)));

        Subscription subscription = Subscription.create(params);
        return subscription.getId();
    }

    public boolean isUserSubscribed(String customerId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("customer", customerId);
            params.put("status", "active");

            List<Subscription> subscriptions = Subscription.list(params).getData();
            return !subscriptions.isEmpty();
        } catch (StripeException e) {
            e.printStackTrace();
            return false;
        }
    }
}

