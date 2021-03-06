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

package com.arm.peliondevicemanagement.services.cache

import com.arm.peliondevicemanagement.components.models.workflow.device.WorkflowDevice
import com.arm.peliondevicemanagement.components.models.workflow.Workflow
import com.arm.peliondevicemanagement.helpers.LogHelper
import com.arm.peliondevicemanagement.helpers.SharedPrefHelper
import com.arm.peliondevicemanagement.services.data.SDATokenResponse
import java.util.concurrent.Executor

class LocalCache(
    private val workflowDao: WorkflowDao,
    private val ioExecutor: Executor
) {

    companion object {
        private val TAG: String = LocalCache::class.java.simpleName
    }

    fun insertWorkflows(workflows: List<Workflow>,
                        insertFinished: () -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "Storing ${workflows.size} workflows")
            workflowDao.insertWorkflows(workflows)
            insertFinished()
        }
    }

    fun updateSDAToken(workflowID: String,
                       sdaToken: SDATokenResponse?,
                       updateFinished: () -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "Updating SDA-Token of workflow: $workflowID")
            workflowDao.updateWorkflowSDAToken(workflowID, sdaToken)
            updateFinished()
        }
    }

    fun updateWorkflowStatus(workflowID: String,
                       workflowStatus: String,
                       updateFinished: () -> Unit) {
        ioExecutor.execute {
            val accountID = SharedPrefHelper.getSelectedAccountID()
            LogHelper.debug(TAG, "Updating status of workflow: $workflowID " +
                    "to $workflowStatus")
            workflowDao.updateWorkflowStatus(accountID, workflowID, workflowStatus)
            updateFinished()
        }
    }

    fun updateWorkflowUploadStatus(workflowID: String,
                             uploadStatus: Boolean,
                             updateFinished: () -> Unit) {
        ioExecutor.execute {
            val accountID = SharedPrefHelper.getSelectedAccountID()
            LogHelper.debug(TAG, "Updating upload-status of workflow: $workflowID " +
                    "to $uploadStatus")
            workflowDao.updateWorkflowUploadStatus(accountID, workflowID, uploadStatus)
            updateFinished()
        }
    }

    fun updateWorkflowDevices(workflowID: String,
                              devices: ArrayList<WorkflowDevice>,
                              updateFinished: () -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "Updating ${devices.size} device of workflow: $workflowID")
            workflowDao.updateWorkflowDevices(workflowID, devices)
            updateFinished()
        }
    }

    fun fetchWorkflowsByStatus(limit: Int,
                               workflowStatus: String,
                               after: String? = null): List<Workflow> {
        LogHelper.debug(TAG, "fetchWorkflowsByStatus()")
        val accountID = SharedPrefHelper.getSelectedAccountID()
        return if(after != null){
            workflowDao.fetchWorkflowByStatus(accountID, limit, workflowStatus, after)
        } else {
            workflowDao.fetchWorkflowByStatus(accountID, limit, workflowStatus)
        }
    }

    fun fetchWorkflowsByMultiStatus(limit: Int,
                                    statusOne: String,
                                    statusTwo: String,
                                    after: String? = null): List<Workflow> {
        LogHelper.debug(TAG, "fetchWorkflowsByMultiStatus()")
        val accountID = SharedPrefHelper.getSelectedAccountID()
        return if(after != null){
            workflowDao.fetchWorkflowByMultiStatus(accountID, limit, statusOne, statusTwo, after)
        } else {
            workflowDao.fetchWorkflowByMultiStatus(accountID, limit, statusOne, statusTwo)
        }
    }

    fun fetchSingleWorkflow(workflowID: String, fetchFinished: (workflow: Workflow) -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "fetchSingleWorkflow()")
            val accountID = SharedPrefHelper.getSelectedAccountID()
            val workflow = workflowDao.fetchSingleWorkflow(accountID, workflowID)!!
            fetchFinished(workflow)
        }
    }

    fun isWorkflowStored(workflowID: String): Boolean {
        val isStored: Boolean
        LogHelper.debug(TAG, "isWorkflowStored()")
        val accountID = SharedPrefHelper.getSelectedAccountID()
        val storedWorkflow = workflowDao.fetchSingleWorkflow(accountID, workflowID)
        isStored = storedWorkflow != null
        return isStored
    }

    fun deleteAllWorkflows(accountID: String,
                           deleteComplete: () -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "Deleting workflows of account: $accountID")
            workflowDao.deleteAllWorkflows(accountID)
            deleteComplete()
        }
    }

    fun deleteAllWorkflowsExceptStatus(accountID: String,
                                       workflowStatus: String,
                           deleteComplete: () -> Unit) {
        ioExecutor.execute {
            LogHelper.debug(TAG, "Deleting all workflows with status != $workflowStatus of account: $accountID")
            workflowDao.deleteAllWorkflowsExceptStatus(accountID, workflowStatus)
            deleteComplete()
        }
    }
}