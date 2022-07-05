//
//  AppDelegate.swift
//  HT450_IOS
//
//  Created by Orangerd on 2022/1/25.
//

import UIKit
import AVFoundation

@main
class AppDelegate: UIResponder, UIApplicationDelegate {



    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        //try? AVAudioSession.sharedInstance().setCategory(.playback)
        
//        do {
//            try AVAudioSession.sharedInstance().setCategory(AVAudioSession.Category.playback, mode: AVAudioSession.Mode.default, options: [.mixWithOthers, .allowAirPlay])
//                    print("Sound_Playback OK")
//                    try AVAudioSession.sharedInstance().setActive(true)
//                    print("Sound_Session is Active")
//                } catch {
//                    print(error)
//                }
        
        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default, options: [.mixWithOthers, .allowAirPlay])
            print("Sound1_Playback OK")
            try AVAudioSession.sharedInstance().setActive(true)
            print("Sound1_Session is Active")
        } catch {
            print("Sound1_",error)
        }
        
        return true
    }

   
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

