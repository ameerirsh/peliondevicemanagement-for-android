/*
 * Copyright 2020 ARM Ltd.
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arm.peliondevicemanagement.components.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arm.peliondevicemanagement.components.listeners.RecyclerItemClickListener
import com.arm.peliondevicemanagement.components.models.LicenseModel
import kotlinx.android.synthetic.main.layout_item_license.view.*

class LicenseViewHolder(itemView: View,
                        private val itemClickListener: RecyclerItemClickListener):
    RecyclerView.ViewHolder(itemView) {

    private lateinit var licenseModel: LicenseModel

    init {
        itemView.setOnClickListener {
            itemClickListener.onItemClick(licenseModel)
        }
    }

    internal fun bind(model: LicenseModel) {
        this.licenseModel = model
        itemView.apply {
            tvTitle.text = model.title
            tvType.text = model.licenseType
        }
    }

}