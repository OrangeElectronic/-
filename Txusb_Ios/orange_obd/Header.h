//
//  Header.h
//  txusb
//
//  Created by 王建智 on 2019/7/16.
//  Copyright © 2019 王建智. All rights reserved.
//
#import <Foundation/Foundation.h>
UInt16 CRC16_User(UInt8 *U8Buffer, UInt16 U16Size,UInt16 U16Remainder, UInt16 U16Polynomial)
{
    UInt16 U16Size_count;
    UInt8 U8Bit_count;
    UInt16    U16CRC=U16Remainder;
    for(U16Size_count=0;U16Size_count<U16Size;U16Size_count++)
    {
        U16CRC ^= (U8Buffer[U16Size_count]<<8);
        for (U8Bit_count=0; U8Bit_count<8; U8Bit_count++)
        {
            if (U16CRC & 0x8000)
            {
                U16CRC =(U16CRC<<1);
                U16CRC =(U16CRC ^U16Polynomial);
            }
            else
            {
                U16CRC =(U16CRC<<1);
            }
        }
    }
    return (U16CRC);
}
