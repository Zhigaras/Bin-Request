package com.zhigaras.binrequest.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.zhigaras.binrequest.R
import com.zhigaras.binrequest.databinding.CardInfoLayoutBinding
import com.zhigaras.binrequest.model.BinReplyModel

const val KEY_PHONE = "phoneNumber"
const val KEY_LOCATION = "location"
const val KEY_WEB_LINK = "webLink"
const val TAG = "myDebug"

class CardInfoViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    
    private var binding: CardInfoLayoutBinding
    private var viewModel = MainViewModelFactory().create(MainViewModel::class.java)
    
    init {
        binding = CardInfoLayoutBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        
        setUpIntentListeners()
        
    }
    
    fun setUpCard(binReply: BinReplyModel) {
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
    }
    
//    fun onClick(textView: TextView) {
//        val phoneNumber = Uri.parse(binding.bankPhoneDescription.text.toString())
//        val intent = Intent(Intent.ACTION_DIAL, phoneNumber)
//        viewModel.getExternalIntent(intent)
//    }
    
    fun setUpIntentListeners() {
        binding.bankPhoneDescription.setOnClickListener {
            Log.d(TAG, "text is clicked")
            val phoneNumber = Uri.parse(binding.bankPhoneDescription.text.toString())
            val intent = Intent(Intent.ACTION_DIAL, phoneNumber)
            viewModel.getExternalIntent(intent)
        }
    }
}