/******************************************************************************
 * Copyright © 2019, Semiconductor Components Industries, LLC
 * (d/b/a ON Semiconductor). All rights reserved.
 *
 * This code is the property of ON Semiconductor and may not be redistributed
 * in any form without prior written permission from ON Semiconductor.
 * The terms of use and warranty for this code are covered by contractual
 * agreements between ON Semiconductor and the licensee.
 *
 * This is Reusable Code.
 *
 * Class Name: CircularProgressView.swift
 ******************************************************************************/

import UIKit
class CircularProgressView: UIView {
    // First create two layer properties
    private var circleLayer = CAShapeLayer()
    private var progressLayer = CAShapeLayer()
    var progress: CGFloat {
        get {
            return progressLayer.strokeEnd
        }
        set {
            if newValue > 1 {
                progressLayer.strokeEnd = 1
            } else if newValue < 0 {
                progressLayer.strokeEnd = 0
            } else {
                progressLayer.strokeEnd = newValue
            }
        }
    }
    override init(frame: CGRect) {
        super.init(frame: frame)
        createCircularPath()
    }
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        createCircularPath()
    }
    func createCircularPath() {
        let circularPath = UIBezierPath(arcCenter: CGPoint(x: frame.size.width / 2.0, y: frame.size.height / 2.0), radius: 60, startAngle: -.pi / 2, endAngle: 3 * .pi / 2, clockwise: true)
        circleLayer.path = circularPath.cgPath
        circleLayer.fillColor = UIColor.clear.cgColor
        circleLayer.lineCap = .round
        circleLayer.lineWidth = 10.0
        circleLayer.strokeColor = UIColor.gray.cgColor
        progressLayer.path = circularPath.cgPath
        progressLayer.fillColor = UIColor.clear.cgColor
        progressLayer.lineCap = .round
        progressLayer.lineWidth = 10.0
        progress = 0
        progressLayer.strokeColor = UIColor.red.cgColor
        layer.addSublayer(circleLayer)
        layer.addSublayer(progressLayer)
    }
    
}
