package com.arm.peliondevicemanagement.components.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arm.peliondevicemanagement.R
import com.arm.peliondevicemanagement.components.models.workflow.WorkflowDeviceModel
import com.arm.peliondevicemanagement.components.viewholders.DeviceViewHolder

class WorkflowDeviceAdapter(private val workflowDeviceList: ArrayList<WorkflowDeviceModel>):
    RecyclerView.Adapter<DeviceViewHolder>() {

    companion object {
        private val TAG: String = WorkflowDeviceAdapter::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(itemView = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.layout_item_device,
                parent,
                false))
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) =
        holder.bind(model = workflowDeviceList[position])

    override fun getItemCount(): Int = workflowDeviceList.size

}