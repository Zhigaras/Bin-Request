package com.zhigaras.binrequest.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.zhigaras.binrequest.R
import com.zhigaras.binrequest.databinding.CardInfoLayoutBinding
import com.zhigaras.binrequest.model.BinReplyModel
import java.util.Locale

const val TAG = "myDebug"

class CardInfoViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    
    private var binding: CardInfoLayoutBinding
    var currentBinReply: BinReplyModel? = null
    
    init {
        binding = CardInfoLayoutBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        setUpClickListeners()
    }
    
    fun setUpCard(binReply: BinReplyModel) {
        currentBinReply = binReply
        binding.apply {
            if (binReply.country?.name != null || binReply.country?.emoji != null) {
                countryDescription.text = buildString {
                    append(binReply.country.name)
                    append(binReply.country.emoji)
                }
            } else {
                countryDescription.text = null
            }
            
            if (binReply.country?.latitude != null || binReply.country?.longitude != null) {
                locationDescription.text = buildString {
                    append("Lat.")
                    append(binReply.country.latitude)
                    append(" : Lon.")
                    append(binReply.country.longitude)
                }
            } else {
                locationDescription.text = null
            }
            bankName.text = binReply.bank?.name
            bankUrl.text = binReply.bank?.url
            bankPhoneDescription.text = binReply.bank?.phone
            cardType.text = binReply.type
            val requireSchemeImg = when (binReply.scheme?.lowercase()) {
                "visa" -> R.drawable.visa
                "mastercard" -> R.drawable.mastercard
                "jcb" -> R.drawable.jcb
                "maestro" -> R.drawable.maestro
                "unionpay" -> R.drawable.unionpay
                "amex" -> R.drawable.american_express
                "discover" -> R.drawable.dinersclub
                else -> android.R.color.transparent
                
            }
            schemeImg.setImageDrawable(
                ContextCompat.getDrawable(context, requireSchemeImg)
            )
        }
    }
    
    
    private fun setUpClickListeners() {
        binding.bankPhoneDescription.setOnClickListener {
            if (!binding.bankPhoneDescription.text.isNullOrEmpty()) {
                val phoneNumber = currentBinReply?.bank?.phone
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel: $phoneNumber")
                )
                val title = "Call: " + PhoneNumberUtils.formatNumber(
                    phoneNumber,
                    Locale.getDefault().country
                )
                startIntent(intent, title)
                
            }
        }
        binding.bankUrl.setOnClickListener {
            if (!binding.bankUrl.text.isNullOrEmpty()) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://" + currentBinReply?.bank?.url)
                )
                val title = intent.data.toString()
                startIntent(intent, title)
            }
        }
        binding.locationDescription.setOnClickListener {
            if (!binding.locationDescription.text.isNullOrEmpty()) {
                val lat = currentBinReply?.country?.latitude
                val lon = currentBinReply?.country?.longitude
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:$lat,$lon?z=14")
                )
                val title = "Locate $lat:$lon"
                    startIntent(intent, title)
            }
        }
    }
    
    private fun startIntent(intent: Intent, title: String) {
        val chooser = Intent.createChooser(intent, title)
        try {
            context.startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Log.d(TAG, e.message.toString())
        }
    }
}