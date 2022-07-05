package com.orange.tpms.service

import android.util.Log
import android.view.KeyEvent
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.ue.kt_frag.Frag_Check_Sensor_Read_IdCopy_Selection
import com.orange.tpms.ue.kt_frag.Frag_Function_New_OBD_ID_copy
import com.orange.tpms.ue.kt_frag.Frag_Function_OBD_ID_copy
import com.orango.electronic.jzutil.hexToByte
import com.orango.electronic.orange_og_lib.PublicBean
import com.orango.electronic.orange_og_lib.Util.OggUtils
import com.orango.electronic.orange_og_lib.Util.keyEventLinstener

/**
 * 當按下按鈕時要實現的操作
 * */
object KeyEventDefine {
    fun keyEvent(event: KeyEvent): Boolean? {
        if ((event.keyCode in 7..16 || event.keyCode in 29..34 || event.keyCode == 67) && event.action == KeyEvent.ACTION_UP) {
            val frag = JzActivity.getControlInstance().getNowPage()
            if (frag is Frag_Function_OBD_ID_copy) {
                return idCopyEvent(event, frag)
            } else if (frag is Frag_Function_New_OBD_ID_copy) {
                return newIdCopyEvent(event, frag)
            }else if(frag is Frag_Check_Sensor_Read_IdCopy_Selection){
                return idKeyIn(event, frag)
            }
        }
        return null
    }
    /**
     * 當畫面為idCopy(舊版)時
     * */
    private fun idCopyEvent(event: KeyEvent, frag: Frag_Function_OBD_ID_copy): Boolean? {
        val act = JzActivity.getControlInstance().getRootActivity()
        val focus = frag.adapter.focus
        if (focus != -1 && focus % 2 == 0) {
            if (PublicBean.position == PublicBean.複製傳感器) {
                Log.e(
                    "dec",
                    "keyCode:${event.keyCode}->" + frag.adapter.beans.OldSemsor[focus / 2].id
                )
                if (event.keyCode in 7..16) {
                    if (frag.adapter.beans.OldSemsor[focus / 2].id.length >= 8) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    if (PublicBean.isDec()) {
                        if ((frag.adapter.beans.OldSemsor[focus / 2]).id.isNotEmpty()) {
                            val temp = frag.adapter.beans.OldSemsor[focus / 2]
                            while (temp.id.length < 8) {
                                temp.id = "0${temp.id}"
                            }
                            val tem =
                                "${OggUtils.byteArrayToInt(temp.id.hexToByte())}${event.keyCode - 7}"
                            frag.adapter.beans.OldSemsor[focus / 2].id =
                                "${Integer.toHexString(tem.toLong().toInt())}"
                        } else {
                            frag.adapter.beans.OldSemsor[focus / 2].id += "${
                                Integer.toHexString(
                                    event.keyCode - 7
                                )
                            }"
                        }

                    } else {
                        frag.adapter.beans.OldSemsor[focus / 2].id += "${event.keyCode - 7}"
                    }
                } else if (event.keyCode in 29..34) {
                    if (PublicBean.isDec()) {
                        return false
                    }
                    if (frag.adapter.beans.OldSemsor[focus / 2].id.length >= 8) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    val array = arrayOf("A", "B", "C", "D", "E", "F")
                    frag.adapter.beans.OldSemsor[focus / 2].id += array[event.keyCode - 29]
                } else {
                    if (frag.adapter.beans.OldSemsor[focus / 2].id.isEmpty()) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    if (PublicBean.isDec()) {
                        val temp = frag.adapter.beans.OldSemsor[focus / 2]
                        while (temp.id.length < 8) {
                            temp.id = "0${temp.id}"
                        }
                        val tem = "${OggUtils.byteArrayToInt(temp.id.hexToByte())}"
                        if (tem.length - 1 > 0) {
                            frag.adapter.beans.OldSemsor[focus / 2].id =
                                "${
                                    Integer.toHexString(
                                        tem.substring(
                                            0,
                                            tem.length - 1
                                        ).toInt()
                                    )
                                }"
                        } else {
                            frag.adapter.beans.OldSemsor[focus / 2].id = ""
                        }
                    } else {
                        val a = frag.adapter.beans.OldSemsor[focus / 2].id
                        frag.adapter.beans.OldSemsor[focus / 2].id =
                            a.substring(0, a.length - 1)
                    }
                }
            }
            frag.adapter.notifyDataSetChanged()
        } else {
            if (event.keyCode in 7..16) {
                if (frag.adapter.beans.NewSensor[focus / 2].id.length >= 8) {
                    return keyEventLinstener.keyEventListener(event, act)
                }
                if (PublicBean.isDec() && PublicBean.position == PublicBean.OBD_RELEARM) {
                    if ((frag.adapter.beans.NewSensor[focus / 2]).id.isNotEmpty()) {
                        var temp = frag.adapter.beans.NewSensor[focus / 2]
                        while (temp.id.length < 8) {
                            temp.id = "0${temp.id}"
                        }
                        val tem =
                            "${OggUtils.byteArrayToInt(temp.id.hexToByte())}${event.keyCode - 7}"
                        frag.adapter.beans.NewSensor[focus / 2].id =
                            "${Integer.toHexString(tem.toLong().toInt())}"
                    } else {
                        frag.adapter.beans.NewSensor[focus / 2].id += "${
                            Integer.toHexString(
                                event.keyCode - 7
                            )
                        }"
                    }

                } else {
                    frag.adapter.beans.NewSensor[focus / 2].id += "${event.keyCode - 7}"
                }
            } else if (event.keyCode in 29..34) {
                if (PublicBean.isDec() && PublicBean.position == PublicBean.OBD_RELEARM) {
                    return false
                }
                if (frag.adapter.beans.NewSensor[focus / 2].id.length >= 8) {
                    return keyEventLinstener.keyEventListener(event, act)
                }
                val array = arrayOf("A", "B", "C", "D", "E", "F")
                frag.adapter.beans.NewSensor[focus / 2].id += array[event.keyCode - 29]
            } else {
                if (frag.adapter.beans.NewSensor[focus / 2].id.isEmpty()) {
                    return keyEventLinstener.keyEventListener(event, act)
                }
                if (PublicBean.isDec()) {
                    var temp = frag.adapter.beans.NewSensor[focus / 2]
                    while (temp.id.length < 8) {
                        temp.id = "0${temp.id}"
                    }
                    val tem = "${OggUtils.byteArrayToInt(temp.id.hexToByte())}"
                    if (tem.length - 1 > 0) {
                        frag.adapter.beans.NewSensor[focus / 2].id = "${
                            Integer.toHexString(
                                tem.substring(
                                    0,
                                    tem.length - 1
                                ).toInt()
                            )
                        }"
                    } else {
                        frag.adapter.beans.NewSensor[focus / 2].id = ""
                    }
                } else {
                    val a = frag.adapter.beans.NewSensor[focus / 2]
                    frag.adapter.beans.NewSensor[focus / 2].id =
                        a.id.substring(0, a.id.length - 1)
                }
            }
            frag.adapter.notifyDataSetChanged()
        }
        return null
    }

    /**
     * 當畫面為idCopy(新版)時
     * */
    private fun newIdCopyEvent(event: KeyEvent, frag: Frag_Function_New_OBD_ID_copy): Boolean? {
        if(!frag.single){
            return null
        }
        val act = JzActivity.getControlInstance().getRootActivity()
        val focus = frag.adapter.focus
        if (focus != -1 && focus % 2 == 0) {
            if (PublicBean.position == PublicBean.複製傳感器) {
                Log.e(
                    "dec",
                    "keyCode:${event.keyCode}->" + frag.adapter.beans.OldSemsor[focus / 2].id
                )
                if (event.keyCode in 7..16) {
                    if (frag.adapter.beans.OldSemsor[focus / 2].id.length >= 8) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    if (PublicBean.isDec()) {
                        if ((frag.adapter.beans.OldSemsor[focus / 2]).id.isNotEmpty()) {
                            val temp = frag.adapter.beans.OldSemsor[focus / 2]
                            while (temp.id.length < 8) {
                                temp.id = "0${temp.id}"
                            }
                            val tem =
                                "${OggUtils.byteArrayToInt(temp.id.hexToByte())}${event.keyCode - 7}"
                            frag.adapter.beans.OldSemsor[focus / 2].id =
                                "${Integer.toHexString(tem.toLong().toInt())}"
                        } else {
                            frag.adapter.beans.OldSemsor[focus / 2].id += "${
                                Integer.toHexString(
                                    event.keyCode - 7
                                )
                            }"
                        }

                    } else {
                        frag.adapter.beans.OldSemsor[focus / 2].id += "${event.keyCode - 7}"
                    }
                } else if (event.keyCode in 29..34) {
                    if (PublicBean.isDec()) {
                        return false
                    }
                    if (frag.adapter.beans.OldSemsor[focus / 2].id.length >= 8) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    val array = arrayOf("A", "B", "C", "D", "E", "F")
                    frag.adapter.beans.OldSemsor[focus / 2].id += array[event.keyCode - 29]
                } else {
                    if (frag.adapter.beans.OldSemsor[focus / 2].id.isEmpty()) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    if (PublicBean.isDec()) {
                        val temp = frag.adapter.beans.OldSemsor[focus / 2]
                        while (temp.id.length < 8) {
                            temp.id = "0${temp.id}"
                        }
                        val tem = "${OggUtils.byteArrayToInt(temp.id.hexToByte())}"
                        if (tem.length - 1 > 0) {
                            frag.adapter.beans.OldSemsor[focus / 2].id =
                                "${
                                    Integer.toHexString(
                                        tem.substring(
                                            0,
                                            tem.length - 1
                                        ).toInt()
                                    )
                                }"
                        } else {
                            frag.adapter.beans.OldSemsor[focus / 2].id = ""
                        }
                    } else {
                        val a = frag.adapter.beans.OldSemsor[focus / 2].id
                        frag.adapter.beans.OldSemsor[focus / 2].id =
                            a.substring(0, a.length - 1)
                    }
                }
            }
            frag.adapter.notifyDataSetChanged()
        } else {
            if (event.keyCode in 7..16) {
                if (frag.adapter.beans.NewSensor[focus / 2].id.length >= 8) {
                    return keyEventLinstener.keyEventListener(event, act)
                }
                if (PublicBean.isDec() && PublicBean.position == PublicBean.OBD_RELEARM) {
                    if ((frag.adapter.beans.NewSensor[focus / 2]).id.isNotEmpty()) {
                        var temp = frag.adapter.beans.NewSensor[focus / 2]
                        while (temp.id.length < 8) {
                            temp.id = "0${temp.id}"
                        }
                        val tem =
                            "${OggUtils.byteArrayToInt(temp.id.hexToByte())}${event.keyCode - 7}"
                        frag.adapter.beans.NewSensor[focus / 2].id =
                            "${Integer.toHexString(tem.toLong().toInt())}"
                    } else {
                        frag.adapter.beans.NewSensor[focus / 2].id += "${
                            Integer.toHexString(
                                event.keyCode - 7
                            )
                        }"
                    }

                } else {
                    frag.adapter.beans.NewSensor[focus / 2].id += "${event.keyCode - 7}"
                }
            } else if (event.keyCode in 29..34) {
                if (PublicBean.isDec() && PublicBean.position == PublicBean.OBD_RELEARM) {
                    return false
                }
                if (frag.adapter.beans.NewSensor[focus / 2].id.length >= 8) {
                    return keyEventLinstener.keyEventListener(event, act)
                }
                val array = arrayOf("A", "B", "C", "D", "E", "F")
                frag.adapter.beans.NewSensor[focus / 2].id += array[event.keyCode - 29]
            } else {
                if (frag.adapter.beans.NewSensor[focus / 2].id.isEmpty()) {
                    return keyEventLinstener.keyEventListener(event, act)
                }
                if (PublicBean.isDec()) {
                    var temp = frag.adapter.beans.NewSensor[focus / 2]
                    while (temp.id.length < 8) {
                        temp.id = "0${temp.id}"
                    }
                    val tem = "${OggUtils.byteArrayToInt(temp.id.hexToByte())}"
                    if (tem.length - 1 > 0) {
                        frag.adapter.beans.NewSensor[focus / 2].id = "${
                            Integer.toHexString(
                                tem.substring(
                                    0,
                                    tem.length - 1
                                ).toInt()
                            )
                        }"
                    } else {
                        frag.adapter.beans.NewSensor[focus / 2].id = ""
                    }
                } else {
                    val a = frag.adapter.beans.NewSensor[focus / 2]
                    frag.adapter.beans.NewSensor[focus / 2].id =
                        a.id.substring(0, a.id.length - 1)
                }
            }
            frag.adapter.notifyDataSetChanged()
        }
        return null
    }
    /**
     * Frag_Check_Sensor_Read_IdCopy_Selection時
     * */
    private fun idKeyIn(event: KeyEvent, frag: Frag_Check_Sensor_Read_IdCopy_Selection):Boolean?{
        val act = JzActivity.getControlInstance().getRootActivity()
        val focus = frag.adapter.focus
        if (focus != -1 ) {
            if (PublicBean.position == PublicBean.複製傳感器) {
                if (event.keyCode in 7..16) {
                    if (frag.adapter.myitem[focus].id.length >= 8) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    if (PublicBean.isDec()) {
                        if ((frag.adapter.myitem[focus].id).isNotEmpty()) {
                            var temp = frag.adapter.myitem[focus]
                            while (temp.id.length < 8) {
                                temp.id = "0${temp.id}"
                            }
                            val tem =
                                "${OggUtils.byteArrayToInt(temp.id.hexToByte())}${event.keyCode - 7}"
                            frag.adapter.myitem[focus].id =
                                "${Integer.toHexString(tem.toLong().toInt())}"
                        } else {
                            frag.adapter.myitem[focus].id += "${
                                Integer.toHexString(
                                    event.keyCode - 7
                                )
                            }"
                        }
                    } else {
                        frag.adapter.myitem[focus].id += "${event.keyCode - 7}"
                    }
                } else if (event.keyCode in 29..34) {
                    if (PublicBean.isDec()) {
                        return false
                    }
                    if (frag.adapter.myitem[focus].id.length >= 8) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    val array = arrayOf("A", "B", "C", "D", "E", "F")
                    frag.adapter.myitem[focus].id += array[event.keyCode - 29]
                } else {
                    if (frag.adapter.myitem[focus].id.isEmpty()) {
                        return keyEventLinstener.keyEventListener(event, act)
                    }
                    if (PublicBean.isDec()) {
                        var temp = frag.adapter.myitem[focus]
                        while (temp.id.length < 8) {
                            temp.id = "0${temp.id}"
                        }
                        val tem = "${OggUtils.byteArrayToInt(temp.id.hexToByte())}"
                        if (tem.length - 1 > 0) {
                            frag.adapter.myitem[focus].id =
                                "${
                                    Integer.toHexString(
                                        tem.substring(
                                            0,
                                            tem.length - 1
                                        ).toInt()
                                    )
                                }"
                        } else {
                            frag.adapter.myitem[focus].id = ""
                        }
                    } else {
                        val a = frag.adapter.myitem[focus].id
                        frag.adapter.myitem[focus].id = a.substring(0, a.length - 1)
                    }
                }
            }
            frag.adapter.notifyDataSetChanged()
        }
        return null
    }
}