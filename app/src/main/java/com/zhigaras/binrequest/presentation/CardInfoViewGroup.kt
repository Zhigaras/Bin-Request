package com.zhigaras.binrequest.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.zhigaras.binrequest.R
import com.zhigaras.binrequest.databinding.CardInfoLayoutBinding
import com.zhigaras.binrequest.model.BinReplyModel

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
        setUpClickAvailability()
    }
    
    fun setUpCard(binReply: BinReplyModel) {
        currentBinReply = binReply
        binding.apply {
            countryDescription.text = buildString {
                append(binReply.country?.name)
                append(binReply.country?.emoji)
            }
            locationDescription.text = buildString {
                append("Lat.")
                append(binReply.country?.latitude)
                append(" : Lon.")
                append(binReply.country?.longitude)
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
                else -> 0
                
                /** To check this!!!!*/
                
            }
            schemeImg.setImageDrawable(
                ContextCompat.getDrawable(context, requireSchemeImg)
            )
        }
        setUpClickAvailability()
    }
    
    private fun setUpClickAvailability() {
        
        binding.bankPhoneDescription.let {
            isClickable = !it.text.isNullOrEmpty()
        }
        binding.bankUrl.let {
            isClickable = !it.text.isNullOrEmpty()
        }
        binding.locationDescription.let {
            isClickable = !it.text.isNullOrEmpty()
        }
    }
    
    private fun setUpClickListeners() {
        binding.bankPhoneDescription.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:" + currentBinReply?.bank?.phone)
            )
            startIntent(intent)
            
        }
        binding.bankUrl.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://" + currentBinReply?.bank?.url)
            )
            startIntent(intent)
            
        }
        binding.locationDescription.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "geo:${currentBinReply?.country?.latitude}," +
                            "${currentBinReply?.country?.longitude}?z=14"
                )
            )
            startIntent(intent)
            
        }
    }
    
    private fun startIntent(intent: Intent) {
        val title = intent.data.toString()
        val chooser = Intent.createChooser(intent, title)
        try {
            context.startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Log.d(TAG, e.message.toString())
        }
    }
}