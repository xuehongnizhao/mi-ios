//
//  UIWebView+JavaScriptAlert.h
//  testuiwbview
//
//  Created by libo on 15/8/14.
//  Copyright (c) 2015年 libo. All rights reserved.
//

#ifndef testuiwbview_UIWebView_JavaScriptAlert_h
#define testuiwbview_UIWebView_JavaScriptAlert_h


@interface UIWebView (JavaScriptAlert)

- (void)webView:(UIWebView *)sender runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(CGRect *)frame;
- (BOOL)webView:(UIWebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(CGRect *)frame;
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex;

@end

#endif
