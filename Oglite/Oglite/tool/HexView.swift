//
//  HexView.swift
//  Oglite
//
//  Created by Jianzhi.wang on 2021/2/26.
//  Copyright © 2021 Jianzhi.wang. All rights reserved.
//

import Foundation
import UIKit
import IQKeyboardManagerSwift
import JzOsTool
protocol RemoveKeyboardDelegate: class {
    func removeKeyboard()
}

class HexButton: UIButton {
    var hexCharacter: String = ""
}

class HexadecimalKeyboard: UIView {
    weak var target   : UIKeyInput?
    weak var delegate : RemoveKeyboardDelegate?
    
    var hexadecimalButtons: [HexButton] = ["0","7","8","9","4","5","6","1","2","3","A","B","C","D","E","F"].map {
        let button = HexButton(type: .system)
        button.hexCharacter = $0
        button.setTitle("\($0)", for: .normal)
        button.backgroundColor = UIColor.white
        button.addTarget(self, action: #selector(didTapHexButton(_:)), for: .touchUpInside)
        return button
    }
    
    var deleteButton: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("⌫", for: .normal)
        button.accessibilityLabel = "Delete"
        button.addTarget(self, action: #selector(didTapDeleteButton(_:)), for: .touchUpInside)
        button.backgroundColor = UIColor.white
        return button
    }()
    
    var okButton: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("OK", for: .normal)
        button.accessibilityLabel = "OK"
        button.addTarget(self, action: #selector(didTapOKButton(_:)), for: .touchUpInside)
        button.backgroundColor = UIColor.white
        return button
    }()
    
    var mainStack: UIStackView = {
        let stackView = UIStackView()
        stackView.distribution = .fillEqually
        stackView.spacing      = 10
        stackView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
        return stackView
    }()
    
    init(target: UIKeyInput) {
        self.target = target
        super.init(frame: .zero)
        configure()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}


// MARK: - Actions

extension HexadecimalKeyboard {
    @objc func didTapHexButton(_ sender: HexButton) {
        if((target! as! UITextField).text!.length<8){
            target?.insertText("\(sender.hexCharacter)")
        }
        if(target! is JzTextField){
            print("HexadecimalKeyboard")
            (target as! JzTextField).textWillChange!((target! as! UITextField).text!)
        }
    }
    
    @objc func didTapDeleteButton(_ sender: HexButton) {
        target?.deleteBackward()
        if(target! is JzTextField){
            print("HexadecimalKeyboard")
            (target as! JzTextField).textWillChange!((target! as! UITextField).text!)
        }
    }
    
    @objc func didTapOKButton(_ sender: HexButton) {
        delegate?.removeKeyboard()
        
    }
}


// MARK: - Private initial configuration methods

private extension HexadecimalKeyboard {
    func configure() {
        self.backgroundColor = UIColor(named: "Gray")
        autoresizingMask     = [.flexibleWidth, .flexibleHeight]
        buildKeyboard()
    }
    
    func buildKeyboard() {
        //MARK: - Add main stackview to keyboard
        mainStack.frame = bounds
        addSubview(mainStack)
        
        //MARK: - Create stackviews
        let panel1         = createStackView(axis: .vertical)
        let panel2         = createStackView(axis: .vertical)
        let panel2Group    = createStackView(axis: .vertical)
        let panel2Controls = createStackView(axis: .horizontal, distribution : .fillProportionally)
 
        
        //MARK: - Create multiple stackviews for numbers
        for row in 0 ..< 3 {
            let panel1Numbers = createStackView(axis: .horizontal)
            panel1.addArrangedSubview(panel1Numbers)
            
            for column in 0 ..< 3 {
                panel1Numbers.addArrangedSubview(hexadecimalButtons[row * 3 + column + 1])
            }
        }
        
        //MARK: - Create multiple stackviews for letters
        for row in 0 ..< 2 {
            let panel2Letters = createStackView(axis: .horizontal)
            panel2Group.addArrangedSubview(panel2Letters)
            
            for column in 0 ..< 3 {
                panel2Letters.addArrangedSubview(hexadecimalButtons[9 + row * 3 + column + 1])
            }
        }
        
        //MARK: - Nest stackviews
        mainStack.addArrangedSubview(panel1)
        panel1.addArrangedSubview(hexadecimalButtons[0])
        mainStack.addArrangedSubview(panel2)
        panel2.addArrangedSubview(panel2Group)
        panel2.addArrangedSubview(panel2Controls)
        panel2Controls.addArrangedSubview(deleteButton)
        panel2Controls.addArrangedSubview(okButton)
        
        //MARK: - Constraint - sets okButton width to two times the width of the deleteButton plus 10 points for the space
        panel2Controls.addConstraint(NSLayoutConstraint(
                                        item       : okButton,
                                        attribute  : .width,
                                        relatedBy  : .equal,
                                        toItem     : deleteButton,
                                        attribute  : .width,
                                        multiplier : 2,
                                        constant   : 10))
    }
    
    func createStackView(axis: NSLayoutConstraint.Axis, distribution: UIStackView.Distribution = .fillEqually) -> UIStackView {
        let stackView = UIStackView()
        stackView.axis         = axis
        stackView.distribution = distribution
        stackView.spacing      = 10
        return stackView
    }
}
