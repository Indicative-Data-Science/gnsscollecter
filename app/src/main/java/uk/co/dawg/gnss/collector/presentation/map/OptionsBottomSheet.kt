package uk.co.dawg.gnss.collector.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uk.co.dawg.gnss.collector.databinding.FragmentMapOptionsBottomSheetBinding

class OptionsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMapOptionsBottomSheetBinding

    private var listener: ActionsListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapOptionsBottomSheetBinding.inflate(inflater, container, false)

        binding.btnFocusLocation.setOnClickListener {
            listener?.onFindMyLocationPressed()
            dismiss()
        }

        binding.btnManualLocation.setOnClickListener {
            listener?.onSelectLocationPressed()
            dismiss()
        }

        return binding.root
    }

    fun setListener(listener: ActionsListener) = apply {
        this.listener = listener
    }


    interface ActionsListener {

        fun onFindMyLocationPressed()

        fun onSelectLocationPressed()

    }
}