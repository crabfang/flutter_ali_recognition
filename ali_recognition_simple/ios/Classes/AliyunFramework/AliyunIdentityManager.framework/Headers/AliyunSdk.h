//
//  ZolozSdk.h
//  ZolozIdentityManager
//
//  Created by richard on 22/11/2017.
//  Copyright © 2017 Alipay. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AliyunSdk : NSObject

+ (void)init;


/*同步获取设备指纹token
1）SDK初始化后需要经过一段时间计算才能获取到deviceToken，建议间隔3秒以上；
2）deviceToken短期有效，不能长期缓存使用，不能作为设备唯一标识。
*@param session 要获取的session缓冲区
*@param lenth   session缓冲区的长度，此值应不小于384   比如：char session[384] = {0};
*@return 0表示成功，其他表示失败
*/
+(int) getSession:(char *) session : (const int) lenth DEPRECATED_MSG_ATTRIBUTE("use getMobileSession instead");


/*同步获取设备指纹token
1）SDK初始化后需要经过一段时间计算才能获取到deviceToken，建议间隔3秒以上；
2）如果没有获取到token，返回的内容是@""，没有内容的字符串。
 */
+(NSString*)getMobileSession;

//获取无线保镖基本数据。
+ (NSDictionary*) getSecurityInfo;

@end
