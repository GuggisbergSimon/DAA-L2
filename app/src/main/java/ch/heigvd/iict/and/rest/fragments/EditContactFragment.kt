package ch.heigvd.iict.and.rest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.heigvd.iict.and.rest.R
import ch.heigvd.iict.and.rest.models.PhoneType
import ch.heigvd.iict.and.rest.viewmodels.ContactsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditContactFragment : Fragment() {

    private val contactsViewModel: ContactsViewModel by activityViewModels()
    private lateinit var nameEditText: EditText
    private lateinit var firstnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var zipEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var phoneTypeGroup: RadioGroup
    private lateinit var phoneNumberEditText: EditText

    private lateinit var cancelButton: Button
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var createButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_contact, container, false)
        nameEditText = view.findViewById(R.id.edit_contact_name)
        firstnameEditText = view.findViewById(R.id.edit_contact_firstname)
        emailEditText = view.findViewById(R.id.edit_contact_email)
        birthdayEditText = view.findViewById(R.id.edit_contact_birthday)
        addressEditText = view.findViewById(R.id.edit_contact_address)
        zipEditText = view.findViewById(R.id.edit_contact_zip)
        cityEditText = view.findViewById(R.id.edit_contact_city)
        phoneTypeGroup = view.findViewById<RadioGroup>(R.id.edit_contact_phonetype)
        phoneNumberEditText = view.findViewById(R.id.edit_contact_phonenumber)

        cancelButton = view.findViewById(R.id.button_cancel)
        deleteButton = view.findViewById(R.id.button_delete)
        saveButton = view.findViewById(R.id.button_save)
        createButton = view.findViewById(R.id.button_create)

        fun saveContact() {
            val name = nameEditText.text.toString()
            val firstname = firstnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val birthday = Calendar.getInstance().apply {
                time = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).parse(birthdayEditText.text.toString())!!
            }
            val address = addressEditText.text.toString()
            val zip = zipEditText.text.toString()
            val city = cityEditText.text.toString()
            val phoneType = when (phoneTypeGroup.checkedRadioButtonId) {
                R.id.edit_contact_phonetype_home -> PhoneType.HOME
                R.id.edit_contact_phonetype_mobile -> PhoneType.MOBILE
                R.id.edit_contact_phonetype_office -> PhoneType.OFFICE
                R.id.edit_contact_phonetype_office -> PhoneType.FAX
                else -> PhoneType.HOME
            }
            val phoneNumber = phoneNumberEditText.text.toString()
            contactsViewModel.saveContact(
                name,
                firstname,
                email,
                birthday,
                address,
                zip,
                city,
                phoneType,
                phoneNumber
            )
        }

        // Handle buttons
        cancelButton.setOnClickListener {
            contactsViewModel.selectContact(null)
            requireActivity().supportFragmentManager.popBackStack()
        }

        deleteButton.setOnClickListener {
            contactsViewModel.deleteContact()
        }

        saveButton.setOnClickListener {
            saveContact()
        }

        createButton.setOnClickListener {
            saveContact()
        }

        // Setup fragment on contact selection
        contactsViewModel.selectedContact.observe(viewLifecycleOwner, Observer { contact ->
            contact?.let {
                nameEditText.setText(it.name)
                firstnameEditText.setText(it.firstname)
                emailEditText.setText(it.email)
                birthdayEditText.setText(
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(it.birthday?.time!!)
                )
                addressEditText.setText(it.address)
                zipEditText.setText(it.zip)
                cityEditText.setText(it.city)
                when (it.type) {
                    PhoneType.HOME -> phoneTypeGroup.check(R.id.edit_contact_phonetype_home)
                    PhoneType.MOBILE -> phoneTypeGroup.check(R.id.edit_contact_phonetype_mobile)
                    PhoneType.OFFICE -> phoneTypeGroup.check(R.id.edit_contact_phonetype_office)
                    PhoneType.FAX -> phoneTypeGroup.check(R.id.edit_contact_phonetype_fax)
                    null -> TODO()
                }
                phoneNumberEditText.setText(it.phoneNumber)
                deleteButton.visibility = View.VISIBLE
                saveButton.visibility = View.VISIBLE
                createButton.visibility = View.GONE
            } ?: run {
                deleteButton.visibility = View.GONE
                saveButton.visibility = View.INVISIBLE
                createButton.visibility = View.VISIBLE
            }
        })

        return view
    }
}