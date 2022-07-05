//
//  Language.swift
//  Peripheral
//
//  Created by 王建智 on 2019/8/27.
//  Copyright © 2019 KoKang Chu. All rights reserved.
//

import Foundation
class Language {
  static  var ChineseTR = ["橙的電子無線胎壓燒錄系統","使用者登入","帳戶","密碼", "登入", "車輛資訊",
      "品牌", "車型", "年份", "燒錄數量", "選擇燒錄輪位(可複選)", "RF (右前輪)", "RR (右後輪)","LR (左後輪)","LF (左前輪)","全選","下一步","重新選擇"
  ,"執行燒錄","新發射器ID號碼","原發射器ID號碼","車輛資訊，請依序選擇品牌、車型、年份與指定燒錄輪位，並按下一步","請依序輸入發射器ID號碼"
  ,"ID號碼已輸入，請按執行燒錄","程式燒錄中，請勿移動發射器......","程式燒錄完成","程式燒錄失敗 (失敗的輪位)","車輛資訊，請依序選擇品牌、車型、年份與燒錄數量，並按下一步"
  ,"錯誤代碼 :","PAD裝置不存在於系統中","PAD裝置連接異常","燒錄中移動發射器","網路連線逾時","無網路連線","下載目錄失敗","掃描器未連接","PAD裝置中途移除"
  ,"PAD裝置載入異常","此錯誤發生在未識別的位置","燒錄程式載入中","網路連線逾時，是否重新登入?","PAD裝置範圍內探索到的發射器數量與選擇的數量不符","登入失敗","學碼步驟","開始"
  ,"將發射器插入USB PAD","錯誤","未連結","檢查更新","USB PAD 驅動程式更新","更新完成","更新失敗","以更新至最新版本","是否要更新","註冊失敗","註冊成功","創建您的帳戶","帳號","E-mail"
  ,"密碼","輸入密碼","再次輸入密碼","個人資料","名字","姓","公司","電話","產品序號","郵寄地址","地區","郵遞區號","街","市","省","至少8個字符","請確認密碼","註冊","取消","郵件已發送","重設密碼","請輸入註冊O-Genius所設定的《電子郵件信箱》"
      ,"發送","請至您的電子信箱收取信件，找到我們發送的電子郵件，主題為重設密碼","僅用於SP201傳感器","語言"];
   static var  English =  ["Orange Electronic TPMS progamming system","User login ","User name","Passward", "Log in", "vehicle selection",
       "make", "model", "year", "Quantity", "Select tire position (multiple choice)", "RF (Right Front)", "RR (Right Rear)","LR (Left Rear)","LF (Left Front)","select all ","NEXT ","Reselect"
   ,"Program sensor","new sensor ID number ","original sensor ID number","Vehicle Selection, please select Make, Model, Year, tire position, and then click \"Next\"","Key in sensor ID number according to position  "
   ,"Enter ID number and  click \"Program sensor\" ","programming, do not move sensor","programming complete ","Fail ( failed tire position )"," vehicle selection , please select make, model ,  year, quantity and then click \"Next\""
   ,"Error code  :","PAD device does not exist in the system","PAD device in abnormal connection ","move the sensor during programming","Internet connection timeout","No internet connection","Download catalogue failed","Scanner not connected","PAD device removed in the midway"
   ,"PAD device loading exception","This error occurred at an unidentified location","Programming formula is loading ","internet connection timeout , do you need to re-login ?","The number of sensor was detected within the PAD device does not match the number selected"
   ,"Login failed","Relearn Procedure","START","Please insert the new sensor into the USB PAD according to position","error","Unlinked","Check for updates","USB PAD driver update","Update completed !","Update failed","To update to the latest version","Do you want to update?","registration failed","Registration success","Create your email account","Account Number","E-mail"
   ,"Password","Choose a password","Repeat password","Personal details","First Name","Last Name","Company","Contact Phone Number","Product serial number","Physical address / Mailing address","Area","ZP_Code","Street","City","State","At least 8 characters","Please confirm your password","Registration","cancel","E-mail has been sent","Reset Password","Please provide the email address that you used when you signed up for O-GENIUS account."
       ,"Submit","Go to your email inbox and find an email from us with the subject “Reset your password”.","Only for SP201 sensor","Languages"];
  static  var ChineseSi = ["橙的电子无线胎压烧录系统","使用者登入","账户","密码", "登入", "车辆信息",
      "品牌", "车型", "年份", "烧录数量", "选择烧录轮位(可复选)", "RFRF (右前轮)", "RR (右后轮)","LR (左后轮)","LF (左前轮)","全选","下一步","重新选择"
  ,"执行烧录","新传感器ID号码","原传感器ID号码","车辆信息，请依序选择品牌、车型、年份与指定烧录轮位，并按下一步","请依序输入传感器ID号码"
  ,"ID号码已输入，请按执行烧录","程序烧录中，请勿移动传感器......","程序烧录完成","程序烧录失败 (失败的轮位)","车辆信息，请依序选择品牌、车型、年份与烧录数量，并按下一步"
  ,"错误代码： ","PAD装置不存在于系统中","PAD装置连接异常","烧录中移动发射器","网路连线逾时","无网路连线","下载目录失败","扫描器未连接","PAD装置中途移除"
  ,"PAD装置载入异常","此错误发生在未识别的位置","烧录程式载入中","网路连线逾时，是否重新登入？","PAD装置范围内探索到的发射器数量与选择的数量不符","登入失败","学码步骤","开始"
  ,"將傳感器插入USB PAD","錯誤","未连结","检查更新","USB PAD驱动程式更新","更新完成","更新失败","以更新至最新版本","是否要更新","注册失败","注册成功","创建您的账户","账号","E-mail"
  ,"密码","输入密码","再次输入密码","个人资料","名字","姓","公司","电话","产品序号","邮件地址","地區","邮政编码","街","市","省","至少8个字符","请确认密码","注册","取消","邮件已发送","重设密码","請輸入註冊O请输入注册O-Genius所设定的《电子邮件信箱》"
      ,"发送","请至您的电子信箱收取信件，找到我们发送的电子邮件，主题为“重设密码”。","仅用于SP201传感器","语言"];
  static  var DE = ["Orange RDKS Programmier-System","eintragen","User Name","Passwort", "eintragen", "Fahrzeug-Auswahl",
      "machen", "Modell", "Jahr", "Menge", "Wählen sie Reifenposition aus (mehrere Möglichkeiten)", "RV (rechts vorne)", "RH (rechts hinten)","LH (links hinten)","LV (links vorne)","wählen sie alle aus ","nächste","Neuauswahl"
  ,"Sensor programmieren","Neue Sensor ID Nummer","Neue Sensor ID Nummer","Fahrzeug-Auswahl, bitte wählen sie Machen, Modell, Jahr, Reifenposition und dann klicken sie \"Nächste\"","Bitte geben sie die Sensor ID Nummer gemäß der Position ein"
  ,"Bestätigen Sie die ID Nummer und klicken \"Sensor programmieren\"","Programmierung, bitte Sensor nicht bewegen","Programmierung beendet","Fehler (Fehler bei Reifenposition)","Fahrzeug-Auswahl, bitte wählen sie Machen, Modell, Jahr, Reifenposition und dann klicken sie \"Nächste\""
  ,"Fehler Code:","PAD konnte im System nicht gefunden werden","PAD nicht korrekt verbunden"," Sensor wurde während der Programmierung bewegt","Internet-Verbindung Zeit abgelaufen","keine Internet-Verbindung","Fehler beim herunterladen des Katalogs","Scanner ist nicht verbunden","Pad wurde in der Mitte entfernt"
  ,"Pad lädt Ausnahme","Dieser Fehler kam wegen einer unbekannten Verbindung","Das Programmier-Formluar wird geladen","Internet-Verbindung Zeit abgelaufen, wollen sie sich nochmals einloggen?","Die Anzahl der im PAD-Gerät erkannten Sensoren stimmt nicht mit der ausgewählten Anzahl überein","Anmeldung fehlgeschlagen"
  ,"Anlern-Prozedur","START","Bitte stecken Sie den Sensor in das USB-PAD.","Error","Nicht verbunden","Suchen Sie nach Updates","USB-PAD-Treiber Aktualisierung","Update abgeschlossen","Update fehlgeschlagen","Um auf die neueste Version zu aktualisieren","Möchten Sie aktualisieren?","Registrierung fehlgeschlagen","Registrierung erfolgreich","erstellen Sie Ihren email account","Acoount Nummer","E-mail"
  ,"Passwort","Wählen Sie ein Passwort aus","wiederholen Sie das Passwort","persönliche Details","Erster Name","Zweiter Name","Firma","Telefon-Nummer","Produkt-Serien-Nummer","persönliche Adresse/Mail-Adresse","Bereich","Postleitzahl","Straße","Stadt","Bundesland","mit mindestens acht Buchstaben","Bitte bestätigen Sie das Passwort","Registrierung","löschen","Die Email wurde verschickt","Passwort zurücksetzen","Bitte stellen sie die e-mail-Adresse zur Verfügung, die sie genommen haben, als sie sich für den O-GENIUS-Account angemeldet haben"
      ,"einreichen","Gehen Sie zu ihrer email-Liste und suchen Sie die email mit dem Namen 'Reset your password'","Nur für SP201 Sensor","Sprachen"];
    
 static   var IT = ["Sistema di programmazione TPMS Orange","Accesso utente","Nome utente","Password", "Accesso", "Seleziona veicolo",
     "Marca", "Modello", "Anno", "Quantità", "Seleziona la posizione del pneumatico (scelta multipla)", "AD (Anteriore destro)", "PD (Posteriore destro)","PS (Posteriore sinistro)","AS (Anteriore sinistro)","Seleziona tutti","Avanti","Riseleziona"
 ,"Programma sensore","Nuovo ID del sensore","ID sensore originale","Selezione veicolo, selezionare marca, modello, anno, posizione pneumatico e poi fare clic su \"avanti\"","Inserire il numero ID in base alla posizione"
 ,"Inserire il numero ID e fare clic su \"Programma sensore\"","Programmazione, non muovere i sensori","Programmazione completata","Fallito (Posizione pneumatico fallita)","Selezione veicolo, selezionare marca, modello, anno, quantità e poi fare clic su \"avanti\""
 ,"Codice Errato","PAD non trovato nel sistema","Connessione errata del PAD","Muovi il sensore durante la Programmazione","Connessione Internet Scaduta","Nessuna connessione Internet","Download del Catalogo fallita","Scanner non Connesso","PAD rimosso durante il processo "
 ,"PAD loading e caricamento","Errore generico non classificato ","La Programmazione è in fase di caricamento","Connessione Internet Scaduta, vuoi riaccedere?","Il numero dei sensori selezionati non corrisponde con il numero dei sensori inseriti nel PAD"
 ,"Accesso fallito","Procedura di apprendimento","START","Inserire i sensorei nell USB PAD","error","Non connesso","Controlla gli aggiornamenti","Driver USB PAD in aggiornamento","aggiornamento completato","Aggiornamento fallito","Per aggiornare all'ultima versione","Vuoi aggiornare?","Registrazione non riuscita","Registrazione avvenuta con successo","Crea con il tuo account di posta elettronica","Numero account","E-mail"
 ,"Password","Scegliere una password","Ripetere la password","Dati personali","Nome","Cognome","Azienda","Numero telefonico","Numero di serie del prodotto","Indirizzo/ indirizzo e-mail","La zona","Codice postale","Via","Città","Stato","Minimo 8 caratteri","Conferma la password","Registrati","Cancella","L'email è stata inviata","Resetta la password","Si prega di fornire l\'indirizzo e-mail che hai usatoquando ti sei registrato per l\'account O-GENIUS."
     ,"Invio","Vai alla tua casella di posta elettronica e troverai un\'e-mail con oggetto \"Resetta la password\"","Solo per sensore SP201","Lingua"];
    static var DA = ["Orange Electronic TPMS progamming system","Bruger login","konto","Password", "Sign in", "Køretøjsoplysninger",
                    "mærker", "modeller", "år", "Forbrændende mængde", "Vælg brændhjul (kan vælges)", "RF (Højer forhjul)", "RR (Højre Baghjul)","LR (Venstre baghjul)","LF (Venstre forhjul)","Vælg alle","Næste","Vælg igen"
        ,"Udfør brænding","Nyt transmitter ID-nummer","Original transmitter ID-nummer","Køretøjsinformation, vælg venligst mærket, modellen, året og det udpegede brænderhjul i rækkefølge, og klik derefter på Næste","Indtast senders ID-nummer i rækkefølge"
        ,"Der er indtastet ID-nummer. Klik for at udføre programmeringen","Flyt ikke senderen under programmeringen......","Programmering afsluttet.","Programming failed. (Dækposition mislykkedes)","Køretøjsinformation, vælg venligst mærke, model, år og antal blink i rækkefølge, og klik derefter på Næste"
        ,"Fejlkode:","PAD-enhed findes ikke i systemet","PAD-enhedens forbindelsesfejl","Mobil launcher under programmering","Netværksforbindelse blev udløbet","Ingen internetforbindelse","Downloadkatalog mislykkedes","Scanneren er ikke tilsluttet","PAD-enheden blev fjernet midtvejs"
        ,"Fejl ved indlæsning af PAD-enhed","Denne fejl opstod på et ukendt sted","Brændende programindlæsning","Netværksforbindelsen er udløbet. Skal du logge ind igen?","Antallet af sendere fundet i PAD-enheden stemmer ikke overens med det valgte antal","Login mislykkedes","Lær proceduren igen","START"
        ,"Indsæt sensoren i USB Holderen.","Fejl","ikke længere tilsluttet.","Søg efter opdateringer","USB Holder driveropdatering","Opdateringen afsluttet!","Opdatering mislykkedes","At opdatere til den nyeste version","Hvorvidt der skal opdateres","Registrering mislykkedes","Registrering godkendt","Opret din e-mail-konto","Kontonummer","E-mail"
        ,"Password","Vælg Password","Gentag Password","Personal details","Fornavn","Efternavn","Firma","Telefon / mobil","Produkt serienummer","Adresse","Område","Postnummer","Gade","By","provinsen","Mindst 8 tegn","Bekræft venligst adgangskoden","Registrering godkendt","Cancel","E-mail sendt","Reset Password","Angiv den e-mail-adresse, du brugte da du tilmeldte dig O-GENIUS-konto."
        ,"Send","Gå til din e-mail-indbakke og find en e-mail fra os med emnet \"Nulstil din adgangskode\".","Kun til SP201-sensor","Sprog"];

    static func getShare(_ name:String)->String{
        let preferences = UserDefaults.standard
        let currentLevelKey = name
        if preferences.object(forKey: currentLevelKey) == nil {
            return "繁體中文"
        } else {
            let currentLevel = preferences.string(forKey: currentLevelKey)!
            return currentLevel
        }
    }
    
    static func writeshare(_ name:String,_ key:String){
        let preferences = UserDefaults.standard
        preferences.set(name,forKey: key)
        let didSave = preferences.synchronize()
        if !didSave {
            print("saverror")
        }
    }
    public static func SetLanguAge(_ it:Int)->String{
        switch Language.getShare("language") {
        case "English":
            return Language.English[it-1]
        case "繁體中文":
            return Language.ChineseTR[it-1]
        case "简体中文":
            return Language.ChineseSi[it-1]
        case "Deutsche":
            return Language.DE[it-1]
        case "Italiano":
            return Language.IT[it-1]
        case "Dansk":
            return Language.DA[it-1]
        default:
            return ""
        }
    }
   
}
