//
//  Util_Play_Sound.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/9.
//
import AVFoundation
import Foundation
class Util_Play_Sound{
   static var instance:Util_Play_Sound? = nil
    
    public static var getInstance:Util_Play_Sound{
        get{
            if(instance==nil){
                instance=Util_Play_Sound()
            }
            return instance!
        }
    }
    var player: AVAudioPlayer?
    func playSound() {
        guard let url = Bundle.main.url(forResource: "translate_tts", withExtension: "mp3") else { return }

        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
            try AVAudioSession.sharedInstance().setActive(true)

            /* The following line is required for the player to work on iOS 11. Change the file type accordingly*/
            player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)

            /* iOS 10 and earlier require the following line:
            player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileTypeMPEGLayer3) */

            guard let player = player else { return }

            player.play()

        } catch let error {
            print(error.localizedDescription)
        }
    }
}
