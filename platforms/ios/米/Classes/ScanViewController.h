//
//  ScanViewController.h
//  米
//
//  Created by libo on 15/11/19.
//
//

#import <UIKit/UIKit.h>
#import "ZBarSDK.h"
@interface ScanViewController : UIViewController <ZBarReaderViewDelegate>
{
  //  ZBarReaderView *readerView;
  //  UITextView *resultText;
    ZBarCameraSimulator *cameraSim;
}
@property (nonatomic, weak) IBOutlet UITextView *resultText;

@property (nonatomic, weak) IBOutlet ZBarReaderView *readerView;

@end
