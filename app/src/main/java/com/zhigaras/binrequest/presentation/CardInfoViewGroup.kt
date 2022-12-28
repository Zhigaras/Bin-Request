package com.zhigaras.binrequest.presentation

import android.content.Context
import android.text.style.TtsSpan.TextBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.zhigaras.binrequest.R
import com.zhigaras.binrequest.databinding.CardInfoLayoutBinding
import com.zhigaras.binrequest.model.BinReplyModel

class CardInfoViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    
    private var binding: CardInfoLayoutBinding
    
    init {
        binding = CardInfoLayoutBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

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
            schemeImg.setImageDrawable(ContextCompat.getDrawable(context, requireSchemeImg)
            )
        }
        
        
    }
}