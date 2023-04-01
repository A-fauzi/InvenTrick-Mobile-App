package com.example.warehouseproject.core.view.main.account_fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.constant.Constant.User.FULLNAME
import com.example.warehouseproject.core.constant.Constant.User.ID
import com.example.warehouseproject.core.constant.Constant.User.PROFILE_PHOTO
import com.example.warehouseproject.core.constant.Constant.User.TOKEN
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.utils.helper.RealtimeDatabase
import com.example.warehouseproject.core.model.user.UserRequest
import com.example.warehouseproject.core.model.user.UserResponse
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.core.view.authentication.SignInActivity
import com.example.warehouseproject.core.view.main.MainActivityPresenter
import com.example.warehouseproject.core.view.main.MainView
import com.example.warehouseproject.core.view.main.account_fragment.account_update.AccountUpdateActivity
import com.example.warehouseproject.core.view.main.account_fragment.privacy_police.PrivacyPoliceActivity
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.api.ProductsUserApiService
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui.ProductsUserActivity
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui.ProductsUserViewModel
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui.ProductsUserViewModelFactory
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductsAdapterPaging
import com.example.warehouseproject.databinding.FragmentAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.coroutines.launch

class AccountFragment : Fragment(), MainView, ProductsAdapterPaging.ProductsListenerPaging {

    private lateinit var realtimeDatabase: RealtimeDatabase

    private lateinit var binding: FragmentAccountBinding

    private lateinit var presenter: MainActivityPresenter

    private lateinit var viewModel: ProductsUserViewModel
    private lateinit var productsAdapterPaging: ProductsAdapterPaging

    private val profileImg = Paper.book().read<String>(PROFILE_PHOTO).toString()
    private val fullname = Paper.book().read<String>(FULLNAME).toString()
    private val division = Paper.book().read<String>("division").toString()
    private val token = Paper.book().read<String>(TOKEN).toString()

    private fun initView() {
        presenter = MainActivityPresenter(requireActivity(), requireActivity(), this, UserApiService())

        realtimeDatabase = RealtimeDatabase(requireActivity())

        productsAdapterPaging = ProductsAdapterPaging(requireActivity(), this)

        // Belom kelar
        viewModel = ViewModelProvider(this, ProductsUserViewModelFactory(ProductsUserApiService.create(token)))[ProductsUserViewModel::class.java]

        lifecycleScope.launch {
            viewModel.listDataProductsUser.collect {
                productsAdapterPaging.submitData(it)
            }
        }

        // Untuk mengecek data count pada paging first
        productsAdapterPaging.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                if (productsAdapterPaging.itemCount < 1) {
                    // data empty state
                    binding.chipProductUpload.text = productsAdapterPaging.itemCount.toString() + " " + "Products"
                } else {
                    // data is not empty
                    binding.chipProductUpload.text = productsAdapterPaging.itemCount.toString() + " " + "Products"
                }
            }
        }

        Paper.init(requireActivity())

        // Set TopBar
        topAppBar()

        binding.tvProfileFullname.text = fullname
        binding.tvDivision.text = division
        Picasso.get().load(profileImg).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivProfile)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        initView()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickButton()

    }

    private fun onClickButton() {
        binding.newTxtTopbar.viewEnd.setOnClickListener {

            popUpMenu()

        }

        binding.ivBtnUpdateProfile.setOnClickListener {
            startActivity(Intent(activity, AccountUpdateActivity::class.java))
        }

        binding.cvBtnLogout.setOnClickListener {
            logOut()
        }

        binding.cvActivityUser.setOnClickListener {
            Toast.makeText(requireActivity(), "Masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.cvProductUploads.setOnClickListener {
            startActivity(Intent(requireActivity(), ProductsUserActivity::class.java))
        }
        binding.cvPrivacy.setOnClickListener {
            startActivity(Intent(requireActivity(), PrivacyPoliceActivity::class.java))
        }
    }

    private fun topAppBar() {
        Picasso.get().load(profileImg).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.newTxtTopbar.viewStart)
        binding.newTxtTopbar.viewEnd.setImageResource(R.drawable.ic_settings)
        binding.newTxtTopbar.txtTopBar.text = getString(R.string.txt_topbar_account)
        binding.newTxtTopbar.cvStatusActivityUser.visibility = View.GONE
        binding.newTxtTopbar.tvStatusActivityUser.visibility = View.GONE

        binding.newTxtTopbar.viewEnd.setOnClickListener {
            binding.newTxtTopbar.viewEnd.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation_button))
            Toast.makeText(requireActivity(), "Masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }

        binding.newTxtTopbar.viewStart.setOnClickListener {
            binding.newTxtTopbar.viewStart.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation_button))
            Toast.makeText(requireActivity(), "Masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessBodyReqStatusView(response: UserResponse.SingleResponse) {
        realtimeDatabase.write(response.data._id, UserRequest.StatusActivity(response.data.status_activity))
    }

    override fun onErrorBodyReqStatusView(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFailureView(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun popUpMenu() {
        val popupMenu = PopupMenu(activity, binding.newTxtTopbar.viewEnd, Gravity.END, 0, R.style.myListPopupWindow)
        popupMenu.menuInflater.inflate(R.menu.top_bar_menu_account, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    // Logout
                    logOut()
                }
            }

            true
        }
        popupMenu.show()
    }

    private fun logOut() {
        val data = UserRequest.StatusActivity("offline")
        val token = Paper.book().read<String>(TOKEN).toString()
        val userId = Paper.book().read<String>(ID).toString()
        presenter.updateStatusActivityUser(token, userId, data)

        Paper.book().destroy()
        val intent = Intent(activity, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onClickItem(data: Product?) {

    }

}