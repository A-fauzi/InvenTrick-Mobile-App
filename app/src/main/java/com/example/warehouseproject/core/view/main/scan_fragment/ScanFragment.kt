package com.example.warehouseproject.core.view.main.scan_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.core.view.main.home_fragment.stock_out_product.StockOutActivity
import com.example.warehouseproject.databinding.FragmentScanBinding
import com.google.zxing.integration.android.IntentIntegrator
import io.paperdb.Paper
import org.json.JSONException

class ScanFragment : Fragment() {

    private lateinit var presenter: ScanPresenter

    private lateinit var binding: FragmentScanBinding

    private lateinit var btnScanState: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(layoutInflater, container, false)

        presenter = ScanPresenter(this@ScanFragment, ScanInteractor())

        Paper.init(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = Paper.book().read<String>(Constant.User.DIVISION).toString()
        when(position) {
            "Receiving" -> { binding.btnClickScanStockOut.visibility = View.GONE }
            "Finish Good" -> { binding.btnClickScanStockIn.visibility = View.GONE }
            else -> {}
        }

        binding.btnClickScanStockIn.setOnClickListener {
            // start presenter scan from camera
            presenter.scanFromCamera()
            btnScanState = "in"
        }

        binding.btnClickScanStockOut.setOnClickListener {
            presenter.scanFromCamera()
            btnScanState = "out"
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null)
        {
            //if qrcode has nothing in it
            if (result.contents == null){
                Toast.makeText(requireActivity(), "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                //if qr contains data
                try {
                    val contents = result.contents

                    val bundle = Bundle()
                    bundle.putString("code_items_key", contents)

                   when(btnScanState) {
                       "in" -> {
                           intentWithContent(bundle, StockInActivity::class.java)
                       }
                       "out" -> {
                           intentWithContent(bundle, StockOutActivity::class.java)
                       }
                       else -> {
                           btnScanState = "null"
                       }
                   }

                }
                catch (e: JSONException) {
                    e.printStackTrace()
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(requireActivity(), result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun intentWithContent(bundle: Bundle, intentClass: Class<*>) {
        val intent = Intent(requireActivity(), intentClass)
        intent.putExtras(bundle)
        startActivity(intent)
        requireActivity().finish()
    }

}