//
//  WebViewControler.swift
//  Zhengxin
//
//  Created by Jianzhi.wang on 2020/11/25.
//

import UIKit

import WebKit
class WebViewController: UIViewController {
    var mWebView: WKWebView? = nil

    override func viewDidLoad() {
        super.viewDidLoad()
        var user=Md_User_Setting.getUserSetting()
        let url = "https://bento2.orange-electronic.com/Zhexing/DataList?admin=\(user.admin)"
        loadURL(urlString: url)
        
    }
    
    private func loadURL(urlString: String) {
        let url = URL(string: urlString)
        if let url = url {
            let request = URLRequest(url: url)
            // init and load request in webview.
            mWebView = WKWebView(frame: self.view.frame)
            if let mWebView = mWebView {
                mWebView.navigationDelegate = self
                mWebView.load(request)
                self.view.addSubview(mWebView)
                self.view.sendSubviewToBack(mWebView)
            }
        }
    }
 
}


extension WebViewController: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
        print(error.localizedDescription)
    }
    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        print("Strat to load")
    }
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        print("finish to load")
    }
}
