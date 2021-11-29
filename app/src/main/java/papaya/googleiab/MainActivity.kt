package papaya.googleiab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.android.billingclient.api.*

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.buyNow)

        val skuList = ArrayList<String>()
        skuList.add("test_prod_1")

        val purchasesUpdatedListener = PurchasesUpdatedListener{
            billingResult, purchses ->
        }

        var billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()

        button.setOnClickListener {

            billingClient.startConnection(object : BillingClientStateListener{
                override fun onBillingServiceDisconnected() {
//                    TODO("Not yet implemented")
                }

                override fun onBillingSetupFinished(billingResult: BillingResult) {
//                    TODO("Not yet implemented")

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK){

                        val params = SkuDetailsParams.newBuilder()
                        params.setSkusList(skuList)
                            .setType(BillingClient.SkuType.INAPP)

                        billingClient.querySkuDetailsAsync(params.build()){
                            billingResult, skuDetailsList ->

                            for (skuDetails in skuDetailsList!!) {
                                val flowPurchase = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()

                                val responseCode = billingClient.launchBillingFlow(this@MainActivity, flowPurchase).responseCode
                            }
                        }
                    }
                }

            })
        }
    }
}