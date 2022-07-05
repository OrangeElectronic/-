//
//  ChineseTr.swift
//  txusb
//
//  Created by 王建智 on 2019/8/5.
//  Copyright © 2019 王建智. All rights reserved.
//

import Foundation
class ChineseTr{
    static let dic=[
    "app_agree":"同意",
    "app_disagree":"不同意",
    "app_cancel":"取消",
    "app_connection":"連線",
    "app_sign_in":"登入",
    "app_registration":"註冊",
    "app_forgot_password":"忘記密碼",
    "Programming":"燒錄中",
    "app_menue":"主選單",
    "app_home_check_sensor":"檢查感測器",
    "app_trigger":"觸發",
    "app_home_program_sensor_phone":"燒錄感測器",
    "app_home_id_copy":"複製感測器ID",
    "app_setup":"系統設定",
    "app_home_obdii_relearn":"車輛OBDII學碼",
    "app_home_cloud_information":"雲端資訊",
    "app_home_online_shopping":"線上訂購",
    "app_user_manual":"使用說明",

    "app_scan_2d_code":"掃描二維碼",
    "app_mmy":"車輛選擇",
    "app_select_car_makes":"選擇車型",
    "app_select_car_model":"選擇車款",
    "app_select_year":"選擇年份",
    "app_number_choice":"選擇燒錄數量",
    "app_start":"開始",
    "app_key_in":"輸入指定感測器ID",
    "app_scan_and_packing":"請掃描感測器外殼或包裝盒上的二維碼",
    "app_program":"燒錄",
    "app_copy_original":"複製原感測器ID",
    "app_original_sensor":"讀取原感測器",
    "app_original_id":"原感測器ID",
    "app_new_sensor":"新發射器ID碼",
    "app_new_sensor_list":"新感測器列表",
    "app_my_favorite":"我的最愛",
    "app_scan":"掃描",

    "app_setting":"系統設定",
    "app_setting_my_favorite":"系統設定/我的最愛",
    "app_my_favorite_setting":"設定我的最愛",
    "app_setting_wifi":"系統設定/WIFI",
    "app_wifi":"WIFI設定",
    "app_setting_bluetooth":"系統設定/藍牙",
    "app_bluetooth":"藍牙",
    "app_my_device":"我的裝置",
    "app_bluebud_other_device":"其它裝置",
    "app_setting_unit":"系統設定/顯示單位t",
    "app_units_tire":"胎壓",
    "app_temp_title":"溫度",
    "app_id_dec_hex":"ID格式(10/16進制)",
    "app_auto":"自動",
    "app_minutes":"分鐘",
    "app_never":"不使用",
    "app_setting_language":"系統設定/語言",
    "app_setting_device_information":"系統設定/裝置資訊",
    "app_device_name":"裝置名稱",
    "app_model":"裝置型號",
    "infomation_serial_number":"裝置序號",
    "infomation_software_version":"軟體版本",
    "app_hardware_version":"硬體版本",
    "app_database_version":"資料庫版本",
    "app_check_for_updates":"檢查更新",
    "app_system_update_auto":"自動更新",
    "app_reset_all_settings":"恢復預設值",
    "app_setting_privacy_policy":"系統設定/隱私權",
    "app_scan_tips":"請掃描目錄或海報上的二維碼",
    "Setting":"設定",
    "Area":"地區",
    "to_give":"應用程式可能會根據您的所在地區為您提供當地內容",
    "Select":"選擇",
    "Languages":"語言",
    "Languages_info":"應用程式會使用列表上的第一個有支援的語言",
    "Privacy_Policy":"隱私權政策",
    "Welcome":"隱私權政策\n非常歡迎您使用「Orange USB TPMS 應用程式 」，當您開始使用我們的產品，即表示您信賴我們對您個人資訊的處理方式。本《隱私權政策》旨在協助您瞭解我們收集的資料類型、收集這些資料的原因以及這些資料的用途及方法。請您詳閱下列內容，若有任何疑問，歡迎與我們連絡sales@orange-electronic.com\n一、隱私權保護政策的適用範圍\n我們的《隱私權政策》適用於本產品的所有服務。我們的《隱私權政策》不適用於由其他公司或個人提供的服務，或我們服務所連結到的其他網站。\n二、個人資料的收集、處理\n• 我們提供產品和服務來幫助您更新產品程式資料、和提升產品功能。當您造訪本產品或使用本產品所提供之功能服務時，我們將視該服務功能性質，請您提供必要的個人資料、或收集一些資訊，並在該特定目的範圍內處理及利用您的個人資料，我們使用資料來增進產品和服務及提供更好的支援。我們只會在徵得您的同意時，才與本公司以外的其他公司、機構或個人分享個人資訊。\n• 我們收集資訊的管道如下:\n1. 您提供的資訊:可能包括但不限於您的姓名、聯絡信箱、連絡電話、產品序號、購買日期、公司名稱、主要銷售品牌、位址、或地址。在建立使用者帳戶或註冊產品、請求提供產品支援或其他服務、請求提供有關產品的資訊、或參與調查等相關情況下可能會要求您提供此類資訊。\n2. 我們經由您使用我們的產品和服務而取得的資訊:我們會針對您使用的服務和使用方式收集相關資訊，可能包括：\n裝置資訊：裝置專屬資訊例如裝置ID編號，包括機器ID、IMEI和/或MEID、產品授權和識別號碼，以及行動電話號碼在內的行動網路資訊等。\n記錄資訊：當您使用本產品時，我們會自動收集特定資訊並儲存在伺服器紀錄中。這類資訊包括：網際網路通訊協定(IP)位址、使用者名稱和帳戶名稱及相關資料、產品系統活動、設定等。\n位置資訊：我們會收集並處理您實際所在位置的相關資訊，包括IP位址、GPS/Wi-Fi/通信網路本地資訊確定的地理位置等。\nCookie和類似技術之使用：Cookie是您在使用我們的產品時，本產品傳送給裝置的一小段文字，可讓我們的產品儲存您的使用資訊。為了提供您最佳的服務，當您使用我們的產品時，我們會使用各種技術收集並儲存資訊，這可能包括使用 Cookie 或類似技術來識別您的裝置。我們使用Cookie的目的有很多，例如：儲存您的偏好語言和其他設定，方便您下次使用我們的產品時套用同樣的設定或提供更實用的服務。我們會透過Cookie將特定活動儲存在本產品上，您可以終止網路連線來停止本產品所有Cookie，不過，請務必記得，如果您停用Cookie，我們有許多服務可能便無法正常運作，例如可能無法記住您的語言偏好設定。\n三、個人資料的使用\n•本公司會將所收集的資訊用於提供、維持、保護與改善服務、開發新的服務，以及保護本公司產品和本公司產品使用者。本公司可能會將您的個人資料用於各種目的，包括：\n1. 回應您的信息要求，並為您提供更有效和高效的客戶服務\n2. 向您提供有關您向本公司購買的產品更新和信息\n3. 分析，市場調查和報告、管理與改善本公司業務\n4. 或適用法律允許或要求。\n5. 本公司也可能使用您的個人信息通過電子郵件與您聯繫，了解本公司產品，服務，促銷活動和特殊活動。\n四、您的選擇權利\n大家對於隱私權的疑慮各有不同，我們的目標是明列我們所收集的資訊，讓您瞭解使用資訊的方式，再做選擇。例如，您可以：\n• 刪除資料：您可自行清除連線裝置的Cookie，或聯絡我們要求刪除帳戶相關資料。\n•變更或更正資料：您可以透過我們的產品帳戶編輯部分個人資料。在部分情況下，特別是資料有誤時，您也可以要求我們變更、更新或訂正您的資料。\n•反對或限制資料使用：您可以要求我們停止使用所有或部分您的個人資料（例如：我們沒有持續使用的法定權利）或限制我們使用（例如：如果您的個人資料不正確或是透過非法取得的）。\n• 讀取和／或獲得您的資料： 您可以要求我們提供您個人資料的副本。\n五、個人資料的保護\n我們致力於保護所持有本公司和使用者的相關資訊，防止未經授權而遭到存取、竄改、揭露或毀損。尤其是：\n• 我們會審查收集、儲存、處理資訊的做法 (包括採取實體安全措施)，保護系統不致發生未經授權的存取。\n•我們僅允許為了代表我們處理個人資訊而需要知悉該等資訊的員工、承包商和代理人存取資訊。且相關人員均須遵守嚴格的契約保密義務，一旦未遵守義務便將受到懲戒或解約處分。\n•我們會定期審查本身產品和服務是否遵守《隱私權政策》。我們收到正式的書面申訴後，將與申訴人聯絡，處理後續事宜。我們會與適當的主管機關 (包括當地的資料保護主管機關) 合作，解決任何有關個人資料移轉且無法由我們直接與使用者解決的申訴。\n六、網站對外的相關連結\n本產品的網頁提供其他網站的網路連結，您也可經由本產品所提供的連結，點選進入其他網站。但該連結網站不適用本產品的隱私權保護政策，您必須參考該連結網站中的隱私權保護政策。\n七、我們分享的資訊\n除非適用下列任一情況，否則我們不會與本公司以外的其他公司、機構或個人分享使用者的個人資訊：\n• 事先徵得您的同意 :\n我們只會在徵得您的同意時，才與本公司以外的其他公司、機構或個人分享個人資訊。分享任何敏感的個人資訊前，我們都會先徵求您的同意。\n• 供外部處理\n我們會將個人資訊提供給我們的關係企業或其他可信賴的公司或人員，請他們根據我們的指示，並遵守本《隱私權政策》和任何其他適當的保密和安全措施，代為處理這類資訊。\n•  基於法律原因\n如果我們真誠地認為，為了達到下列目的，對個人資訊的存取、使用、保存或揭露屬合理必要行為時，我們將與 本公司以外的其他公司、機構或個人分享個人資訊：\n1. 符合任何適用法律、法規、法律函狀或強制性政府調閱要求的規定。\n2. 執行適用《服務條款》，包括調查可能的違規情事。\n3. 偵測、防止或以其他方式處理詐欺、安全或技術問題。\n4. 依法律規範應在合法範圍內，保護本公司、使用者或公眾之權利、財產或安全不致遭受危害。\n我們可能會對外公開並與合作夥伴(例如發佈商、廣告客戶或連結網站)分享不含身分識別內容的資訊。舉例來說，我們可能會將資訊對外公開，讓外界瞭解本公司服務的一般使用趨勢。\n如果本公司涉及合併、收購事宜或出售資產，我們將繼續嚴守任何個人資訊之保密，並於個人資訊移轉或應適用不同隱私權政策前，先行通知受影響的使用者。\n八、隱私權保護政策之變更\n我們的《隱私權政策》會不時變更。我們不會在未經您明確同意的情況下，即縮減本《隱私權政策》賦予您的權利。隱私權政策變更時一律會在本產品APP上發佈；如果屬於重大變更，我們會提供更明顯的通知，讓您在選擇繼續使用我們的產品前，有機會審閱修訂後的政策。",
    "Agree":"同意",
    "Disagree":"不同意",
    "Sign_in":"登入",
    "E_mail":"E-mail",
    "Registration":"註冊",
    "Password":"密碼",
    "Forgot_password":"忘記密碼",
    "Reset_Password":"重設密碼",
    "Og_email":"請輸入註冊O-Genius所設定的《電子郵件信箱》 ",
    "Submit":"發送",
    "Go_email":"請至您的電子信箱收取信件，找到我們發送的電子郵件，主題為“重設密碼\"",
    "Account_creation":"建立帳戶",
    "Create_your_email_account":"創建您的帳戶",
    "Account_Number":"帳號",
    "Choose_a_password":"輸入密碼",
    "At_least_8_characters":"至少8個字符",
    "Repeat_password":"再次輸入密碼",
    "Product_serial_number":"產品序號",
    "Personal_details":"個人資料",
    "First_Name":"名字",
    "Last_Name":"姓",
    "Company":"公司",
    "Contact_Phone_Number":"電話",
    "Physical_address_Mailing_address":"郵寄地址",
    "Country":"區域",
    "Street":"街",
    "City":"市",
    "State":"省",
    "ZP_Code":"郵遞區號",
    "Registration_success":"註冊成功",
    "Orange_USB_TPMS":"Orange USB TPMS",
    "Data_Loading":"載入資訊",
    "Orange_USB_TPMS_Application":"Orange USB TPMS Application",
    "Program_USB_TPMS":"USB CABLE",
    "Program_USB_PAD":"USB PAD",
    "Cloud_information":"雲端數據",
    "Online_shopping":"線上購物",
    "Users_manual":"使用說明",
    "Log_out":"登出",
    "Are_you_sure_you_want_to_log_out":"你確定要退出嗎？",
    "Yes":"是",
    "cancel":"取消",
    "ProgramUSB_TPMS":"Program (USB TPMS)",
    "Download_MYY_data":"載入車輛數據",
    "ID_COPY":"ID COPY",
    "Program":"Program ",
    "Methods_of_vehicle_data_selection":"選擇車輛數據方法",
    "Scan_Code":"掃描QR碼",
    "Vehicle_data_selection":"車輛數據選擇",
    "Please_scan_the_QR_Code_on_the_catalog_or_poster":"請掃描發射器或包裝盒上的ID QR碼",
    "Select_CAR_Make":"選擇車型",
    "Select_CAR_Model":"選擇車款",
    "Select_Year":"選擇年份",
    "Please_insert_the_sensor_into_the_USB_PAD":"將發射器插入USB PAD",
    "Unlinked":"未連結",
    "START":"開始",
    "MENU":"選單",
    "Programming_completed":"程序燒錄完成",
    "Please_remove_the_sensor":"請取下發射器",
    "Programming_failed":"程序燒錄失敗",
    "Please_press_RE_PROGRAM":"請按重新燒錄",
    "RE_PROGRAM":"重新燒錄",
    "Relearn_Procedure":"學碼步驟",
    "Key_in_the_original_sensor_ID_number":"輸入原發射器ID號碼",
    "sensor_ID_number":"發射器ID號碼",
    "ID_code_should_be_8_characters":"ID碼需為8個字元",
    "Bluetooth_pairing_requirements":"藍牙配對要求",
    "would_like_to_with_pair":"USB PAD將與您手機進行配對",
    "Cancel":"取消",
    "Pair":"配對",
    "Program_sensor":"開始燒錄",
    "Programming_do_not_move_sensors":"燒錄中請勿移動發射器",
    "Next":"下一步",
    "error":"錯誤",
    "Programming_failed_where":"燒錄失敗",
    "Get_location":"必須開啟定位功能才能啟動藍芽服務.",
    "Prog_Sensor":"重新燒錄",
    "Please_Confirm_That_The_Sensor_Is_Connect":"請確認傳感器已經連接",
    "Sound":"音效",
    "AreaLanguage":"區域和語言",
    "Version_update":"更新",
    "ProvideUsbTpmsApp":"提供USB TPMS APP\n燒錄模式说明",
    "step1_1":"步驟1.\n請將USB TPMS燒錄線連接手機與sensor",
    "step1_2":"步驟2.\n點選 Program(USB TPMS) ",
    "step1_3":"步驟3.\n1. 確認燒錄線已連線\n2. 確認你的燒錄模式\n3. 選擇確認車輛資訊方式",
    "step1_4":"步驟4-1.\n按下掃描鍵後，請掃描目錄上您要的車輛資訊的二維碼，並跳至步驟6",
    "step1_5":"步驟4-2.\n按下車輛資訊鍵後，依序選擇品牌 → 選擇車型 → 選擇年份",
    "step1_6":"步驟5.\n確認提示欄車輛資訊正確，並按下\"START\"進行燒錄 ",
    "step1_7":"步驟6.\n燒錄完成後,如需續燒錄相同資料sensor,請更換下一顆sensor,按下PROG.SENSOR",
    "step1_8":"步驟7.\n燒錄錯誤，請按下RE-PROGRAM按鍵，重新執行燒錄",
    "step2_1":"步驟1.\n請將USB TPMS燒錄線連接手機與sensor",
    "step2_2":"步驟2.\n點選 Program(USB TPMS)",
    "step2_3":"步驟3.\n1. 確認燒錄線已連線\n2. 確認你的燒錄模式\n3. 選擇確認車輛資訊方式",
    "step2_4":"步驟4-1.\n按下掃描鍵後，請掃描目錄上您要的車輛資訊的二維碼，並跳至步驟6",
    "step2_5":"步驟4-2.\n按下車輛資訊鍵後，依序選擇品牌 → 選擇車型 → 選擇年份",
    "step2_6":"步驟5.\n選擇原發射器ID號碼輸入方式",
    "step2_7":"步驟6.\n1. 輸入原本Sensor ID號碼\n2. 按下PROGRAM 進行燒錄",
    "step2_8":"步驟7.\n燒錄完成後，如需繼續複製其他sensor，請更換下一顆sensor，按下PROG.SENSOR",
    "step2_9":"步驟8.\n燒錄錯誤，請按下RE-PROGRAM按鍵，重新執行燒錄 ",
    "step3_1":"步驟1.\n請先連接電源線，確認USB PAD是否有過電(底部綠燈亮起)，再將預燒錄的sensor放入",
    "step3_2":"步驟2.\n點選 Program(USB PAD) ",
    "step3_3":"步驟3.\nUSB PAD會與您的手機進行藍芽配對\n( 需確定手機定位功能已開啟)",
    "step3_4":"步驟4.\n 1. 確認USB PAD 已連線\n2. 確認你的燒錄模式\n3. 選擇確認車輛資訊方式 ",
    "step3_5":"步驟5-1.\n按下掃描鍵後，請掃描目錄上您要的車輛資訊的二維碼，並跳至步驟6",
    "step3_6":"步驟5-2.\n按下車輛資訊鍵後，依序選擇品牌 → 選擇車型 → 選擇年份",
    "step3_7":"步驟6.\n選擇車輛資訊後，將自動依序讀取PAD中的sensor，讀取完成後請按下 PROGRAM SENSOR ",
    "step3_8":"步驟7.\n燒錄完成後，如需燒錄相同資料的sensor，請更換USB PAD中的sensor，並按下PROG.SENSOR ",
    "step3_9":"步驟8.\n燒錄錯誤，請按下RE-PROGRAM按鍵，重新執行燒錄 ",
    "step4_1":"步驟1.\n請先連接電源線，確認USB PAD是否有過電(底部綠燈亮起)，再將預燒錄的sensor放入",
    "step4_2":"步驟2.\n點選 Program(USB PAD) ",
    "step4_3":"步驟3.\nUSB PAD會與您的手機進行藍芽配對\n( 需確定手機定位功能已開啟)  ",
    "step4_4":"步驟4.\n1. 確認USB PAD 已連線\n2. 確認你的燒錄模式\n3. 選擇確認車輛資訊方式 ",
    "step4_5":"步驟5-1.\n按下掃描鍵後，請掃描目錄上您要的車輛資訊的二維碼，並跳至步驟6",
    "step4_6":"步驟5-2.\n按下車輛資訊鍵後，依序選擇品牌 → 選擇車型 → 選擇年份",
    "step4_7":"步驟6.\n選擇原發射器ID號碼輸入方式",
    "step4_8":"步驟7.\n請依照輪位順序輸入原始Sensor ID, 輸入完後請按下一步",
    "step4_9":"步驟8.\nUSB TPMS 將自動依序讀取PAD中的sensor，讀取完成後請按下 PROGRAM SENSOR",
    "step4_10":"步驟9.\n燒錄完成後，如需複製其他的sensor，請更換USB PAD中的sensor，並按下PROG.SENSOR",
    "step4_11":"步驟10.\n燒錄錯誤，請按下RE-PROGRAM按鍵，重新執行燒錄 ",
    "Set_up":"確認",
    "Check_for_updates":"檢查更新",
    "update":"更新",
    "Automatic_update":"自動更新",
    "USB_TPMS_APP_will_automatically_install_updates":"USB TPMS APP將自動動安裝更新",
    "paired_with_your_device":"配對中",
    "SelectDevice":"選擇設備",
    "For_developer_information":"關於開發者資訊，請訪問網站：www.orange-electronic.com",
    "Updating":"更新中",
    "Please_scan_the_correct_QR_code":"請掃描正確的QR code",
    "norelarm":"學碼流程尚未完成，請耐心等待，我們會儘快更新",
    "confirm_password":"請確認密碼",
    "be_register":"裝置已被註冊",
    "signfall":"登入失敗",
    "nointernet":"网路连线逾时",
    "store_type":"商店类型",
    "Distributor":"经销商",
    "Retailer":"轮胎行",
    "nointer":"网路连线逾时",
    "errorpass":"帐号或密码错误",
    "update_success":"更新成功",
    "need_upadte":"請更新至最新版本",
    "is_latest":"以更新至最新版本",
    "SayQusition":"您好，請詳細說明產品問題",
    "PleaseMessage":"請輸入信件內容",
    "notempty":"訊息不得為空",
    "sendsuccess":"傳送成功",
    "sendfalse":"傳送失敗",
    "minuteago":"1分鐘前",
    "dayago":"1天前",
    "secago":"剛剛",
    "Online_customer_service":"線上客服",
    "Customer_service_specialist":"客服專員",
    "hourago":"1小時前",
    "monthago":"1個月前",
    "yeargo":"1年前",
    "checkscan":"請確認要燒錄的發射器以放入USB PAD， 再點選輪位進行掃描",
    "Scan_Two":"請掃描目錄或海報上的二維碼",
    "keyin":"手動輸入",
    "app_sensor_info_read":"讀取感測器",
    "app_press":"按下",
    "app_to_trigger_sensor":"按下觸發鍵",
    "app_sensor_info_position":"檢查傳感器安裝位置",
    "Notall":"並非所有OE傳感器都包含溫度、電池和電壓狀態",
    "app_voltage":"電壓:",
    "array_language_english":"English",
    "array_language_simch":"简体中文",
    "array_language_trach":"繁體中文",
    "array_language_italian":"italiano",
    "array_language_german":"Deutsch",
    "app_reset":"重置",
    "app_connecting":"連線中…",
    "app_content_empty":"內容不能為空",
    "app_default":"默認",
    "app_device_information":"裝置資訊",
    "app_sounds_vibration":"聲音及振動",
    "app_sleep":"休眠",
    "app_sensor_quantity":"選擇燒錄數量",
    "app_bat":"電池:",
    "app_to_program_sensor":"按下觸發鍵",
    "app_blue_bud":"藍牙",
    "app_wifi_setting":"Wifi",
    "app_blue_bud_open":"打開",
    "app_wifi_connected":"無線網絡連接",
    "app_connected_failed":"連接失敗",














    "app_wifi_disconnected":"Wifi Disconnected",
    "app_permission":"Permission",
    "app_permission_requre":"Our function requires you to open relevant permissions",
    "app_permission_reject":"You have denied the relevant permission. The function is abnormal. Please go to settings to open the relevant permission",
    "app_email_send":"E-mail has bean send",
    "app_wifi_connecting":"Wifi Connection…",
    "app_sign_ing":"Sign in…",
    "app_registrating":"Registration…",
    "app_updating":"更新中…",
    "app_update_completed":"Update completed",
    "app_data_uploading":"Data uploading…",
    "app_data_reading":"Data reading…",
    "app_loading_data":"Loading data…",
    "app_scaning":"Scaning…",
    "app_new_version_detect":"檢測到新版本，是否要更新？",
    "app_bootaloader_mode":"Currently in bootaloader mode, unable to operate, please burn flash.",
    "app_burning_module_firmware":"Burning module firmware",
    "app_updating_model_database":"Updating model database",
    "app_burning_succeeded":"Burning succeeded!",
    "app_module_firmware_updated":"Module firmware has been updated. Do you want to update?",
    "app_vehicle_payment_updated":"There is an update to the vehicle payment database. Do you want to update it?",
    "app_sendor_firmware_updated":"Sensor firmware has been updated. Do you want to update it?",
    "app_model_database_finished":"Model database update finished!",
    "app_writing_model_database":"Writing to model database",
    "app_wiFi_scan_failed":"WiFi scan failed",
    "app_myfavorite_is_empty":"My favorite is empty now!",
    "app_invalid_mmy_qrcode":"Invalid mmy QR code",
    "app_invalid_sensor_qrcode":"Invalid sensor ID QR code",
    "app_account_password_empty":"Account or password cannot be empty",
    "app_regist_success":"Register was successful",
    "app_wrong_format":"Wrong format",
    "app_re_program":"重新燒錄",
    "jz.289":"Sensor repeated",
    "app_checking":"Checking…",
    "app_scan_code_timeout":"Scan code timeout",
    "app_duplicate_items":"There are duplicate items. Please delete them and try again",
    "app_fillin_all_sensor_id":"Please fill in all sensor IDS",
    "app_login_failed":"Login failed",
    "app_no_sensor_set":"No sensor is set, please add it first",
    "app_sensor_copy_different":"The number of sensor IDs to be copied is different",
    "app_no_data_to_copy":"No data to copy",
    "app_module_is_empty":"Module is empty!",
    "app_year_is_empty":"Year is empty!",
    "app_read_failed":"讀取失敗，請重新讀取！",
    "app_name":"TPMS",
    "app_sure":"Sure",
    "app_ok":"OK",
    "app_ssid":"SSID",
    "app_tigger":"觸發",
    "app_back":"Back",
    "app_check":"Check",
    "app_id_clear":"ID",
    "app_fr":"RF",
    "app_rr":"RR",
    "app_rl":"LR",
    "app_fl":"LF",
    "app_pcs":"pcs",
    "app_orange_sensor":"(Orange sensor)",
    "app_copy_new":"CREATE NEW SENSOR",
    "app_psi":"PSI",
    "app_temp":"°C",
    "app_bat_clear":"BAT",
    "app_check_up":"CHECK",
    "app_wifi_choose":"CHOOSE A NETWORK",
    "app_wifi_ask":"Ask to join Networks",
    "app_wifi_auto_tips":"Known networks will be automatically. If no known networks are avalible ,you will be have to manualy select a networks.",
    "app_connected":"Connected",
    "app_disconnected":"Disconnected",
    "app_bluebud_my_device":"My Device",
    "app_bluebud_my_devicename":"ORANGE TPMS PAD",
    "app_bluebud_other_devicename":"ORANGE FNNB01",
    "app_units_temp":"Temperature",
    "app_units_numeral":"Numeral system",
    "app_sounds_vibrate":"Vibrae",
    "app_sounds_ranger_alerts":"Ranger and Alerts",
    "app_sounds_shocl":"Shock",
    "app_system_update":"System Update",
    "app_system_reset":"System Reset",
    "app_system_on":"On",
    "app_system_password_tips":"Please enter your password",
    "app_version":"OG 1.0.1",
    "app_app_name":"Orange Electronic",
    "app_size":"822.1MB",
    "app_tips":"For develop information, please visite the website:www.orange-electronic.com",
    "app_reset_password":"Reset Password",
    "app_reset_tips":"Please provide the email address that you used when you signed up for O-GENIUS account.",
    "app_reset_email":"Go to your email inbox and find an email from us with the subject “Reset your password”.",
    "app_submit":"Submit",
    "app_register_title":"Account Setup",
    "app_register_account_title":"Create your email account",
    "app_register_account_number":"Account Number",
    "app_register_password":"Password",
    "app_register_password_hint":"At least 8 characters",
    "app_register_password_repeat":"Repeat password",
    "app_register_choose_password":"Choose a password",
    "app_register_product":"Product serial number",
    "app_register_personal":"Personal details",
    "app_register_firstname":"First Name",
    "app_register_lastname":"Last Name",
    "app_register_company":"Company",
    "app_register_contact":"Contact Phone Number",
    "app_register_address_title":"Physical address / Mailing address",
    "app_register_country":"Country",
    "app_register_street":"Street",
    "app_register_city":"City",
    "app_register_state":"State",
    "app_register_zpcode":"ZP Code",
    "app_register_sign":"Sign up",
    "app_orange_co":"Orange Electronic Co., Ltd",
    "array_autolock_1":"1 minute",
    "array_autolock_3":"3 minutes",
    "array_autolock_5":"5 minutes",
    "array_autolock_ten":"10 minutes",
    "array_autolock_thirty":"30 minutes",
    "array_autolock_never":"Never",
    "array_area_europe":"Europe",
    "array_area_north_america":"North America",
    "array_area_asia":"Asia",
    "array_unit_c":"C",
    "array_unit_f":"F",
    "array_unit_psi":"Psi",
    "array_unit_bar":"Bar",
    "array_unit_kpa":"Kpa",
    "array_unit_auto":"Auto",
    "array_unit_dec":"Dec",
    "array_unit_hex":"Hex",
    "original_way":"Enter original sensor ID code",
    "new_way":"Enter new sensor ID code",
    "autolock":"更改睡眠定時器設置",
    "updatefault":"update false,Please reconnect wifi and try again!",
    "enginer":"工程模式",
    "maxsize":"max size is 4",
    "wantreprogram":"您要重新加載嗎？",
    "app_blue_bud_close":"關閉",
    "app_unit":"顯示單位",
    "app_language_setting":"Language",
    "app_sensor_info":"SENSOR INFORMATION",
    "app_id":"ID:",
    "app_kpa":"Kpa:",
    "app_c":"°C:",
    "infomation_name":"Name",
    "infomation_module":"Module",
    "infomation_version":"Version",
    "infomation_data_version":"Data Version",
    "idcopyobd":"複製ID (OBD)",
    "prusbpad":"燒錄發射器(USB PAD)",
    "idusbpad":"複製ID (USB PAD)",
    "jz.404":"讀取",
    "wait":"請稍後…",

    "transfer":"開始傳輸",
    "jz.407":"車輛(MMY)選擇錯誤",

    "Please_remove_OBD_and_reinsert":"請移除OBD並重新插入",
    "Please_start_clockwise_from_the_left_front_wheel":"請從左前輪順時針開始",
    "Detecting":"正在檢測……",
    "Choose_the_tire_position_and_enter_sensor_ID_number":"選擇輪胎位置並輸入發射器ID碼",
    "Verification_successfully":"驗證成功",
    "Verification_fail":"驗證失敗",
    "Keep_other_sensors_7_cm_away":"其他發射器請保持7cm以上的距離",
    "Add_vehicle":"新增車輛",
    "New_Password":"新密碼",
    "Confirmation_Password":"確認密碼",
    "Auto_detection":"自動偵測",
    "Add_or_remove_vehicles":"添加或刪除車輛輛資訊",
    "Detection_failed_please_select_another_method":"沒有車輛資訊，請選擇其他方法",
    "Incorrect_information":"資訊不正確",
    "Do_you_want_to_re_loading":"您要重新載入嗎？",
    "Please_provide_the_email_address_that":"請輸入註冊O-Genius所設定的《電子郵件信箱》",
    "ResetPass_Submit":"發送",
    "Setting_reset_password":"重設密碼",
    "Reset_Email":"請至您的電子信箱收取信件，找到我們發送的電子郵件，主題為“重設密碼",]
}
