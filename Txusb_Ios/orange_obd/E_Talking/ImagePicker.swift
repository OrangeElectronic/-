//
//  ImagePicker.swift
//  txusb
//
//  Created by 王建智 on 2019/10/6.
//  Copyright © 2019 王建智. All rights reserved.
//

import Foundation
import UIKit
 import Firebase
class ImagePicker: UIViewController, UIImagePickerControllerDelegate,
UINavigationControllerDelegate {
     let act=(UIApplication.shared.delegate as! AppDelegate).act!
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    var selInmage:UIImage?
    //选取相册
      func fromAlbum() {
        //判断设置是否支持图片库
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary){
            //初始化图片控制器
            let picker = UIImagePickerController()
            //设置代理
            picker.delegate = self
            //指定图片控制器类型
            picker.sourceType = .photoLibrary
            //弹出控制器，显示界面
            self.present(picker, animated: true, completion: {
                () -> Void in
            })
        }else{
            print("读取相册错误")
        }
    }
     
    //选择图片成功后代理
    internal func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        guard let pickedImage = info[.originalImage] as? UIImage else {
            selInmage=nil
            fatalError("Expected a dictionary containing an image, but was provided the following: \(info)")
        }
        selInmage=pickedImage
        ImageReback()
        dismiss(animated: true, completion: nil)
    }
    func ImageReback(){
        
    }
     
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
