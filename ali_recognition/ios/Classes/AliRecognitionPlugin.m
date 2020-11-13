#import "AliRecognitionPlugin.h"
#if __has_include(<ali_recognition/ali_recognition-Swift.h>)
#import <ali_recognition/ali_recognition-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "ali_recognition-Swift.h"
#endif

@implementation AliRecognitionPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAliRecognitionPlugin registerWithRegistrar:registrar];
}
@end
