//
//  UIWebView+JavaScriptAlert.m
//  testuiwbview
//
//  Created by libo on 15/8/14.
//  Copyright (c) 2015年 libo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "UIWebView+JavaScriptAlert.h"

@implementation UIWebView (JavaScriptAlert)

static BOOL diagStat = NO;
static bool isModal = YES;

- (void)webView:(UIWebView *)sender runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(CGRect *)frame
{
    
    NSLog(@"-------");
    
    NSArray * arr = [message componentsSeparatedByString:@"@"];
   
    NSString * title;
    NSString * msg;
    
    if(message){
        
    }else if([message characterAtIndex:0] == '@'){
        title = @"提示";
        msg = [arr objectAtIndex:0];
    }else if([message characterAtIndex:message.length-1]){
        title = [arr objectAtIndex:0];
        msg=@"";
    }else{
        title = [arr objectAtIndex:0];
        msg = [arr objectAtIndex:1];
    }
    
    
    for(NSString * a in arr){
        NSLog(@"%@" , a);
    }
    
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                        message:message
                                                       delegate:self
                                              cancelButtonTitle:@"好吧"
                                              otherButtonTitles:nil];
    isModal = YES;
    

    [alertView show];
    

    while (isModal) {
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
    }

    
 
}

- (BOOL)webView:(UIWebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(CGRect *)frame
{
    NSString * str=message;
    
    //NSAttributedString * attrStr = [[NSAttributedString alloc] initWithData:[str dataUsingEncoding:NSUnicodeStringEncoding] options:@{ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType } documentAttributes:nil error:nil];
    
    NSArray * arr = [str componentsSeparatedByString:@"@"];
    
    for(NSString * a in arr){
        NSLog(@"%@" , a);
    }
    
    
    
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@""
                                                        message:str
                                                       delegate:self
                                              cancelButtonTitle:@"取消"
                                              otherButtonTitles:@"确定", nil];
    
   
    
    
    UITextField *accountName = [[UITextField alloc]init];
    accountName.frame = CGRectMake(25, 55, 234, 35);
    
    [alertView addSubview:accountName];
    
    
    NSLog(@"ccc111");
    isModal = YES;
    
    [alertView show];
    
    NSLog(@"ccc222");
    while (isModal) {
         [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
    }
    NSLog(@"ccc333");
    
    
    return diagStat;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{

    isModal = NO;
    
    if (buttonIndex == 0) {
        diagStat = NO;

    } else if (buttonIndex == 1) {
        diagStat = YES;

        
    }

}



@end
