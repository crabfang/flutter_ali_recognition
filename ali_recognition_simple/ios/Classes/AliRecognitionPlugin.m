#import "AliRecognitionPlugin.h"
#import <AliyunIdentityManager/AliyunIdentityPublicApi.h>

@implementation AliRecognitionPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"AliRecognition"
            binaryMessenger:[registrar messenger]];
  AliRecognitionPlugin* instance = [[AliRecognitionPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    result(@{@"message":@"ok"});
    return;
    if ([@"init" isEqualToString:call.method]) {
        [AliyunSdk init];
        result(@{@"message":@"ok"});
    } else if ([@"getMetaInfos" isEqualToString:call.method]) {
        NSDictionary *info = [AliyunIdentityManager getMetaInfo];
        result(info);
    } else if ([@"verify" isEqualToString:call.method]) {
        NSString *certifyId = call.arguments[@"certifyId"];
        NSMutableDictionary *extParams =  [NSMutableDictionary dictionaryWithDictionary:call.arguments[@"extParams"]];
  //          UIWindow *window = ((AppDelegate*)([UIApplication sharedApplication].delegate)).window;
  //          [extParams setValue:window.rootViewController forKey:@"currentCtr"];
        [[AliyunIdentityManager sharedInstance] verifyWith:certifyId extParams:extParams onCompletion:^(ZIMResponse *response) {
            result(@{@"code" : @(response.code), @"reason": response.reason, @"certifyId":certifyId });
        }];
    } else {
      result(FlutterMethodNotImplemented);
    }
}

@end
