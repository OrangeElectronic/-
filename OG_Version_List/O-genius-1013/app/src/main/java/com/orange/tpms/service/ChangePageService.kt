package com.orange.tpms.service

import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.ue.kt_frag.*
import com.orange.tpms.ue.tire_hotel.Frag_Home_Selection
import com.orango.electronic.orange_og_lib.PublicBean

/**
 * 當選完mmy時，畫面切換的定義
 * */
object ChangePageService {
    fun changePage() {
        if (PublicBean.position != PublicBean.檢查傳感器 && (JzActivity.getControlInstance()
                .getNowPage() !is Frag_Relearm_Detail_Next && PublicBean.position != PublicBean.學碼步驟) && PublicBean.position
            != PublicBean.TireHotel_insert && PublicBean.position != PublicBean.TireHotel && PublicBean.position != PublicBean.複製傳感器
            && PublicBean.position != PublicBean.燒錄傳感器
        ) {
            JzActivity.getControlInstance()
                .changePage(
                    Frag_Relearm_Detail_Next(),
                    "Frag_Relearm_Detail_Next",
                    true
                )
            return
        }
        when (PublicBean.position) {
            PublicBean.TireHotel -> {
                JzActivity.getControlInstance().changePage(
                    Frag_Home_Selection(),
                    "Frag_Home_Selection",
                    true
                )
            }
            PublicBean.TireHotel_insert -> {
                JzActivity.getControlInstance()
                    .changePage(
                        Frag_Check_Sensor_Read(),
                        "Frag_Check_Sensor_Read",
                        true
                    )
            }
            PublicBean.檢查傳感器 -> {
                JzActivity.getControlInstance()
                    .changePage(
                        Frag_Check_Sensor_Read(),
                        "Frag_Check_Sensor_Read",
                        true
                    )
            }
            PublicBean.燒錄傳感器 -> {
                JzActivity.getControlInstance().changePage(
                    Frag_Program_Number_Choice(),

                    "Frag_Program_Number_Choice",
                    true
                )
            }
            PublicBean.複製傳感器 -> {
                if (PublicBean.wheelCount() == 2) {
                    JzActivity.getControlInstance().changePage(
                        Frag_Motor_Select(false),
                        "Frag_Motor_Select",
                        true
                    )
                } else {
                    /**
                     * 兩種ID_Copy選擇方式
                     * */
                    /*--新版--*/
                    JzActivity.getControlInstance().changePage(
                        Frag_New_Idcopy_Selection(),
                        "Frag_Function_ID_Copy_Selection",
                        true
                    )
                    /*--舊版--*/
//                        getControlInstance().changePage(
//                            Frag_Function_ID_Copy_Selection(),
//                            "Frag_Function_ID_Copy_Selection",
//                            true
//                        )
                }
            }
            PublicBean.學碼步驟 -> {
                JzActivity.getControlInstance().changePage(
                    Frag_Relearm_Detail(),
                    "Frag_Relearm_Detail",
                    true
                )
            }
            PublicBean.ID_COPY_OBD -> {
                if (PublicBean.wheelCount() == 2) {
                    JzActivity.getControlInstance().changePage(
                        Frag_Motor_Select(false),
                        "Frag_Motor_Select",
                        true
                    )
                } else {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_ID_Copy_Selection(),
                        "Frag_Obd_Relearm",
                        true
                    )
                }
            }
            PublicBean.OBD_RELEARM -> {
                if (PublicBean.wheelCount() == 2) {
                    JzActivity.getControlInstance().changePage(
                        Frag_Motor_Select(false),
                        "Frag_Motor_Select",
                        true
                    )
                } else {
                    JzActivity.getControlInstance().changePage(
                        Frag_Function_ID_Copy_Selection(),
                        "Frag_Obd_Relearm",
                        true
                    )
                }
            }
            else -> {
                JzActivity.getControlInstance().changePage(
                    Frag_Relearm_Detail(),
                    "Frag_Relearm_Detail",
                    true
                )
            }
        }
    }
}