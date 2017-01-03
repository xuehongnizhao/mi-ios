//
//  Alipay.m
//  米
//
//  Created by libo on 15/8/7.
//
//

#import <Foundation/Foundation.h>
#import "Alipay.h"
#import <AlipaySDK/AlipaySDK.h>
#import <Cordova/CDV.h>



@implementation Alipay


- (void) mipay:(CDVInvokedUrlCommand*)command
{
    
    NSLog(@"mipay------");
    @try {
        
        NSString* payStr = [command.arguments objectAtIndex:0];
        
        NSString* appScheme = @"mialipay";

        
        [[AlipaySDK defaultService] payOrder:payStr fromScheme:appScheme callback:^(NSDictionary *resultDic) {
      
            int succFlag = 1;
            NSString* rs = [resultDic objectForKey:@"resultStatus"];
            
            if([rs isEqualToString:@"9000"])
            {
                NSString* sub = [resultDic objectForKey:@"result"];
                
                NSRange rang = [sub rangeOfString:@"success=\"true\""];
                if(rang.location != NSNotFound){
                    
                    succFlag = 0;
                }
            }
            CDVPluginResult* pluginResult = nil;
            
            NSString* result = [[NSString alloc] init];
            if(succFlag == 1){
                result = @"fail";
            }else{
                result = @"succ";
            }
            
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:result];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
           

        }];
        
        
        
        
        
    }@catch (NSException* exception) {
        
        CDVPluginResult* pluginResult = nil;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_JSON_EXCEPTION messageAsString:[exception reason]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
   }
    
 }

@end

