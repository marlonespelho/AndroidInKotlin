package br.com.espelho.AnexosApp.util

import android.content.Context
import android.text.InputType
import android.text.method.NumberKeyListener
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText


class EditTextTelefone : EditText {
    private var isUpdating: Boolean = false

    /*
	 * Maps the cursor position from phone number to masked number... 1234567890
	 * => (12) 3456-7890
	 */
    private val positioning = intArrayOf(1, 2, 3, 6, 7, 8, 9, 11, 12, 13, 14, 15)

    val cleanText: String
        get() {
            val text = this@EditTextTelefone.text.toString()

            text.replace("[^0-9]*".toRegex(), "")
            return text

        }

    private val keylistenerNumber = KeylistenerNumber()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context,
        attrs,
        defStyle
    ) {
        initialize()

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()

    }

    constructor(context: Context) : super(context) {
        initialize()

    }

    private fun initialize() {

        val maxNumberLength = 11
        this.keyListener = keylistenerNumber

        this.setText("(  )     -     ")
        this.setSelection(1)

        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val current = s.toString()

                /*
				 * Ok, here is the trick... calling setText below will recurse
				 * to this function, so we set a flag that we are actually
				 * updating the text, so we don't need to reprocess it...
				 */
                if (isUpdating) {
                    isUpdating = false
                    return

                }

                /* Strip any non numeric digit from the String... */
                var number = current.replace("[^0-9]*".toRegex(), "")
                if (number.length > 11)
                    number = number.substring(0, 11)

                val length = number.length

                /* Pad the number to 10 characters... */
                val paddedNumber = padNumber(number, maxNumberLength)

                /* Split phone number into parts... */
                val ddd = paddedNumber.substring(0, 2)
                val part1 = paddedNumber.substring(2, 6)
                val part2 = paddedNumber.substring(6, 11).trim { it <= ' ' }

                /* build the masked phone number... */
                val phone = "($ddd) $part1-$part2"

                /*
				 * Set the update flag, so the recurring call to
				 * afterTextChanged won't do nothing...
				 */
                isUpdating = true
                this@EditTextTelefone.setText(phone)

                this@EditTextTelefone.setSelection(positioning[length])

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }
        })
    }

    protected fun padNumber(number: String, maxLength: Int): String {
        var padded = number
        for (i in 0 until maxLength - number.length)
            padded = padded + " "
        return padded

    }

    private inner class KeylistenerNumber : NumberKeyListener() {

        override fun getInputType(): Int {
            return InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        }

        override fun getAcceptedChars(): CharArray {
            return charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        }
    }
}