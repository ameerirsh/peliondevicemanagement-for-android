/*
 * Copyright (c) 2018, Arm Limited and affiliates.
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

package com.arm.peliondevicemanagement.services.cache

import com.arm.peliondevicemanagement.components.models.workflow.WorkflowModel
import com.arm.peliondevicemanagement.helpers.LogHelper
import com.arm.peliondevicemanagement.services.data.SDATokenResponse
import java.util.concurrent.Executor

class LocalCache(
    private val workflowDao: WorkflowDao,
    private val ioExecutor: Executor
) {

    companion object {
        private val TAG: String = LocalCache::class.java.simpleName
    }

    fun insertWorkflows(workflows: List<WorkflowModel>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "Storing ${workflows.size} workflows")
            workflowDao.insertWorkflows(workflows)
            insertFinished()
        }
    }

    fun updateSDAToken(workflowID: String, sdaToken: SDATokenResponse?, updateFinished: () -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "Updating SDA-Token of workflow: $workflowID")
            workflowDao.updateWorkflowSDAToken(workflowID, sdaToken)
            updateFinished()
        }
    }

    fun fetchWorkflows(limit: Int? = null, after: String? = null): List<WorkflowModel> {
        LogHelper.debug(TAG, "fetchWorkflows()")
        return if(limit != null && after != null){
            workflowDao.fetchWorkflows(limit, after)
        } else if(limit != null && after == null) {
            workflowDao.fetchWorkflows(limit)
        } else {
            workflowDao.fetchWorkflows()
        }
    }
}