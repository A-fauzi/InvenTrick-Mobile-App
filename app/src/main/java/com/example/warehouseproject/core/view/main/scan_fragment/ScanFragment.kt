package com.example.warehouseproject.core.view.main.scan_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.warehouseproject.core.view.main.home_fragment.stock_in_product.StockInActivity
import com.example.warehouseproject.databinding.FragmentScanBinding
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException

class ScanFragment : Fragment() {

    private lateinit var presenter: ScanPresenter

    private lateinit var binding: FragmentScanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(layoutInflater, container, false)

        presenter = ScanPresenter(this@ScanFragment, ScanInteractor())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // start presenter scan from camera
        presenter.scanFromCamera()

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

                    val intent = Intent(requireActivity(), StockInActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    requireActivity().finish()

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

}