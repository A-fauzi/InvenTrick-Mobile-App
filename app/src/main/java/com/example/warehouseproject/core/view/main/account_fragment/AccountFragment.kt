package com.example.warehouseproject.core.view.main.account_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.warehouseproject.core.constant.Constant.User.FULLNAME
import com.example.warehouseproject.core.constant.Constant.User.ID
import com.example.warehouseproject.core.constant.Constant.User.PROFILE_PHOTO
import com.example.warehouseproject.core.constant.Constant.User.TOKEN
import com.example.warehouseproject.domain.modelentities.product.Product
import com.example.warehouseproject.core.utils.helper.RealtimeDatabase
import com.example.warehouseproject.domain.modelentities.user.request.UserAuthRequestModel
import com.example.warehouseproject.domain.modelentities.user.response.UserResponseModel
import com.example.warehouseproject.core.service.user.UserApiService
import com.example.warehouseproject.presentation.view.authentication.SignInActivity
import com.example.warehouseproject.core.view.main.MainActivityPresenter
import com.example.warehouseproject.core.view.main.MainView
import com.example.warehouseproject.core.view.main.account_fragment.account_update.AccountUpdateActivity
import com.example.warehouseproject.core.view.main.account_fragment.privacy_police.PrivacyPoliceActivity
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.api.ProductsUserApiService
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui.ProductsUserActivity
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui.ProductsUserViewModel
import com.example.warehouseproject.core.view.main.account_fragment.product_upload_user.ui.ProductsUserViewModelFactory
import com.example.warehouseproject.core.view.main.home_fragment.product_list_all.paging.ui.ProductsAdapterPaging
import com.example.warehouseproject.databinding.ComponentItemDashboardBinding
import com.example.warehouseproject.databinding.FragmentAccountBinding
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.coroutines.launch

class AccountFragment : Fragment(), MainView, ProductsAdapterPaging.ProductsListenerPaging {

    private lateinit var componentProductUpload: ComponentItemDashboardBinding
    private lateinit var componentActivity: ComponentItemDashboardBinding
    private lateinit var componentPrivacy: ComponentItemDashboardBinding
    private lateinit var productsAdapterPaging: ProductsAdapterPaging
    private lateinit var realtimeDatabase: RealtimeDatabase
    private lateinit var viewModel: ProductsUserViewModel
    private lateinit var presenter: MainActivityPresenter
    private lateinit var binding: FragmentAccountBinding

    private val profileImg = Paper.book().read<String>(PROFILE_PHOTO).toString()
    private val division = Paper.book().read<String>("division").toString()
    private val fullName = Paper.book().read<String>(FULLNAME).toString()
    private val token = Paper.book().read<String>(TOKEN).toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        viewModel = ViewModelProvider(this, ProductsUserViewModelFactory(ProductsUserApiService.create(token)))[ProductsUserViewModel::class.java]
        presenter = MainActivityPresenter(requireActivity(), requireActivity(), this, UserApiService())
        productsAdapterPaging = ProductsAdapterPaging(requireActivity(), this)
        realtimeDatabase = RealtimeDatabase(requireActivity())

        componentActivity = binding.viewIncludeActivity
        componentProductUpload = binding.viewIncludeProductUpload
        componentPrivacy = binding.viewIncludePrivacy
        viewComponentDashboard(componentActivity, R.color.green, R.drawable.ic_time_past, getString(R.string.activity), "news")
        viewComponentDashboard(componentProductUpload, R.color.yellow, R.drawable.ic_folder_upload, "Product Upload", "load...")
        viewComponentDashboard(componentPrivacy, R.color.red_smooth, R.drawable.ic_shield_check, "Privacy Police", "action")

        Picasso.get().load(profileImg).placeholder(R.drawable.ic_people).error(R.drawable.img_example).into(binding.ivProfile)
        binding.tvProfileFullname.text = fullName
        binding.tvDivision.text = division

        Paper.init(requireActivity())
        setViewModel()
        setAdapter()
        topAppBar()

    }

    private fun setAdapter() {
        productsAdapterPaging.addLoadStateListener { loadState ->
            val productCount = productsAdapterPaging.itemCount
            if (loadState.append.endOfPaginationReached) {
                if (productsAdapterPaging.itemCount < 1) {
                    // data empty state
                    componentProductUpload.tvChip.text = "$productCount Products"
                } else {
                    // data is not empty
                    componentProductUpload.tvChip.text = "$productCount Products"
                }
            }
        }
    }

    private fun setViewModel() {
        lifecycleScope.launch {
            viewModel.listDataProductsUser.collect {
                productsAdapterPaging.submitData(it)
            }
        }
    }

    private fun viewComponentDashboard(bindingView: ComponentItemDashboardBinding, bgColorStateList: Int, ivIcon: Int, txtComponent: String, textChip: String? = null) {
        bindingView.cvIcon.setCardBackgroundColor(context?.getColorStateList(bgColorStateList))
        bindingView.ivIcon.setImageResource(ivIcon)
        bindingView.tvComponent.text = txtComponent
        bindingView.tvChip.text = textChip
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

        componentActivity.cvActivityUser.setOnClickListener {
            Toast.makeText(requireActivity(), "Masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        componentProductUpload.cvActivityUser .setOnClickListener {
            startActivity(Intent(requireActivity(), ProductsUserActivity::class.java))
        }
        componentPrivacy.cvActivityUser.setOnClickListener {
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

    override fun onSuccessBodyReqStatusView(response: UserResponseModel.SingleResponse) {
        realtimeDatabase.write(response.data._id, UserAuthRequestModel.StatusActivity(response.data.status_activity))
    }

    override fun onErrorBodyReqStatusView(message: String) {
        Log.d("AccountFragment", message)
    }

    override fun onFailureView(message: String) {
        Log.d("AccountFragment", message)
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
        Paper.book().destroy()

        val data = UserAuthRequestModel.StatusActivity("offline")
        val token = Paper.book().read<String>(TOKEN).toString()
        val userId = Paper.book().read<String>(ID).toString()

        presenter.updateStatusActivityUser(token, userId, data)

        val intent = Intent(activity, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onClickItem(data: Product?) {

    }

}