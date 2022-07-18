package com.example.shoppinglist.billing

import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*

class BillingManager(private val activity: AppCompatActivity) { //lesson 58

    private var bClient: BillingClient? = null

    private fun setupBillingClient() {
        bClient = BillingClient.newBuilder(activity)
            .setListener(getPurchaseListener())
            .enablePendingPurchases()
            .build()
    }

    private fun getPurchaseListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { bResult, list ->
            run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {

                }
            }
        }
    }

    private fun nonConsumableItem(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (purchase.isAcknowledged) {
                val acParams =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                        .build()
                bClient?.acknowledgePurchase(acParams) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {

                    }
                }
            }
        }
    }

}