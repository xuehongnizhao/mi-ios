#import "ContactPicker.h"
#import <Cordova/CDVAvailability.h>
#define IOS9  [[[UIDevice currentDevice] systemVersion] floatValue] >= 9.0f
@implementation ContactPicker
    @synthesize callbackID;
    
- (void) chooseContact:(CDVInvokedUrlCommand*)command{
    self.callbackID = command.callbackId;
    if (IOS9) {
        [self open];
    }
    ABPeoplePickerNavigationController *picker = [[ABPeoplePickerNavigationController alloc] init];
    picker.peoplePickerDelegate = self;
    [self.viewController presentModalViewController:picker animated:YES];
}
- (void)open{
    CNContactPickerViewController *contactPickerViewController=[[CNContactPickerViewController alloc]init];
    contactPickerViewController.delegate=self;
    //该对象保存整个通讯录，可以遍历出来
    CNContactStore *contactStore=[[CNContactStore alloc]init];
    
    
    //判断是否授予过权限。(应用第一次打开通讯录一般运行)
    if ([CNContactStore authorizationStatusForEntityType:CNEntityTypeContacts]==CNAuthorizationStatusNotDetermined) {
        //如果没有，弹窗申请权限
        [contactStore requestAccessForEntityType:CNEntityTypeContacts completionHandler:^(BOOL granted, NSError * _Nullable error) {
            //如果允许
            if (granted) {
                NSLog(@"权限允许");
                [self.viewController presentViewController:contactPickerViewController animated:YES completion:^{
                }];
                
            }
            //不允许
            else
            {
                NSLog(@"权限不允许");
            }
        }];
    }
    //如果不是第一次访问通讯录，判断是否有允许访问
    if ([CNContactStore authorizationStatusForEntityType:CNEntityTypeContacts] == CNAuthorizationStatusAuthorized) {
        NSLog(@"有权限访问");
        [self.viewController presentViewController:contactPickerViewController animated:YES completion:^{
        }];
    }
    //不允许访问
    else
    {
        /*UIAlertController *alertController=[UIAlertController alertControllerWithTitle:@"提醒" message:@"没有权限访问，请开启权限" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *action=[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleCancel handler:nil];
        [alertController addAction:action];
        [self.viewController presentViewController:alertController animated:YES completion:nil];
        NSLog(@"没有权限访问");*/
    }
    return;
}
- (void)addContact:(CDVInvokedUrlCommand *)command{
    self.callbackID = command.callbackId;
    
    ABNewPersonViewController *newPersonController = [[ABNewPersonViewController alloc] init];
    newPersonController.newPersonViewDelegate = self;
    UINavigationController *navigationController = [[UINavigationController alloc] initWithRootViewController:newPersonController];
    
    [self.viewController presentViewController:navigationController animated:YES completion:nil];
}
    
- (NSString *)imageURLForRecord:(ABRecordRef)person fullName:(NSString *)fullName {
    CFDataRef imageData = ABPersonCopyImageData(person);
    if (!imageData) {
        return @"";
    }
    NSData *data = (__bridge NSData *)(imageData);
    
    NSString *tmpDirectory = NSTemporaryDirectory();
    NSString *fileName = [NSString stringWithFormat:@"%@_image.png", [fullName isEqualToString:@""] ? [NSDate date] : fullName];
    NSString *imagePath = [tmpDirectory stringByAppendingPathComponent:fileName];
    [data writeToFile:imagePath atomically:YES];
    
    CFRelease(imageData);
    
    return [NSURL fileURLWithPath:imagePath].absoluteString;
}
    
- (NSMutableDictionary *)convertToDictionary:(ABRecordRef)person {
    NSString *fullName, *email;
    fullName = (__bridge NSString*)ABRecordCopyCompositeName(person);
    if (!fullName) {
        fullName = @"";
    }
    
    ABMultiValueRef multi = ABRecordCopyValue(person, kABPersonEmailProperty);
    if(ABMultiValueGetCount(multi) > 0)
    email = (__bridge NSString *)ABMultiValueCopyValueAtIndex(multi, 0);
    else
    email = @"";
    
    ABMultiValueRef multiPhones = ABRecordCopyValue(person, kABPersonPhoneProperty);
    
    NSMutableDictionary* phones = [NSMutableDictionary dictionaryWithCapacity:2];
    
    for(CFIndex i = 0; i < ABMultiValueGetCount(multiPhones); i++) {
        NSString *label = (__bridge NSString*)ABMultiValueCopyLabelAtIndex(multiPhones, i);
        
        label = (__bridge NSString*)ABMultiValueCopyLabelAtIndex(multiPhones, i);
        NSLog(@"Phone Label: %@", label);
        
        [phones setObject:(__bridge NSString*)ABMultiValueCopyValueAtIndex(multiPhones, i) forKey: label];
    }
    
    ABMultiValueRef multiAddresses = ABRecordCopyValue(person, kABPersonAddressProperty);
    NSMutableArray *addresses = [NSMutableArray array];
    
    for (CFIndex i = 0; i < ABMultiValueGetCount(multiAddresses); i++) {
        NSDictionary *dictionary = (__bridge NSDictionary *)(ABMultiValueCopyValueAtIndex(multiAddresses, i));
        
        NSArray *keys = @[(__bridge NSString *)kABPersonAddressStreetKey, (__bridge NSString *)kABPersonAddressCityKey,
                          (__bridge NSString *)kABPersonAddressStateKey, (__bridge NSString *)kABPersonAddressCountryKey];
        
        NSMutableArray *values = [NSMutableArray array];
        
        for (NSString *key in keys) {
            NSString *value = dictionary[key];
            if (value && ![value isEqualToString:@""]) {
                [values addObject:value];
            }
        }
        
        [addresses addObject:[values componentsJoinedByString:@", "]];
    }
    
    NSString *imageURL = [self imageURLForRecord:person fullName:fullName];
    
    NSLog(@"%@ %@", fullName, email);
    
    NSMutableDictionary* contact = [NSMutableDictionary dictionaryWithCapacity:2];
    if (email) {
    }
    [contact setObject:email forKey: @"email"];
    [contact setObject:fullName forKey: @"displayName"];
    [contact setObject:phones forKey:@"phones"];
    contact[@"photoUrl"] = imageURL;
    contact[@"address"] = addresses;
    
    ABRecordID recordID = ABRecordGetRecordID(person); // ABRecordID is a synonym (typedef) for int32_t
    [contact setObject:@(recordID) forKey:@"id"];
    return contact;
}
    
- (void)respondToJS:(NSMutableDictionary *)contact {
    [super writeJavascript:[[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:contact] toSuccessCallbackString:self.callbackID]];
    [self.viewController dismissModalViewControllerAnimated:YES];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:contact];
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackID];
}
    
- (void)newPersonViewController:(ABNewPersonViewController *)newPersonView didCompleteWithNewPerson:(ABRecordRef)person{
    NSMutableDictionary *contact;
    if (person) {
        contact = [self convertToDictionary:person];
    }
    else { // If the user taps cancel, person is passed as NULL. This code block can be moved to convertToDictionary:
        //        contact = @{@"email" : @"",
        //                    @"displayName" : @"",
        //                    @"id" : @""}; // either create a dictionary with the same keys as expected, but with empty strings as values.
        
        //        contact = @{}; //, create an empty dictionary
        
        //        contact = nil; // or keep it as nil.
    }
    
    
    [self respondToJS:contact];
}
    
- (BOOL)peoplePickerNavigationController: (ABPeoplePickerNavigationController *)peoplePicker
      shouldContinueAfterSelectingPerson:(ABRecordRef)person {
    
    NSMutableDictionary *contact;
    contact = [self convertToDictionary:person];
    
    [self respondToJS:contact];
    
    return NO;
}
    
- (void)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker didSelectPerson:(ABRecordRef)person{
    [self peoplePickerNavigationController:peoplePicker shouldContinueAfterSelectingPerson:person];
}
    
- (BOOL) personViewController:(ABPersonViewController*)personView shouldPerformDefaultActionForPerson:(ABRecordRef)person property:(ABPropertyID)property identifier:(ABMultiValueIdentifier)identifierForValue
    {
        return YES;
    }
    
- (void)peoplePickerNavigationControllerDidCancel:(ABPeoplePickerNavigationController *)peoplePicker{
    [self.viewController dismissModalViewControllerAnimated:YES];
    [super writeJavascript:[[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                              messageAsString:@"People picker abort"]
                            toErrorCallbackString:self.callbackID]];
}
    
    /*!
     * @abstract Invoked when the picker is closed.
     * @discussion The picker will be dismissed automatically after a contact or property is picked.
     */
- (void)contactPickerDidCancel:(CNContactPickerViewController *)picker{
    [self.viewController dismissModalViewControllerAnimated:YES];
    [super writeJavascript:[[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                              messageAsString:@"People picker abort"]
                            toErrorCallbackString:self.callbackID]];
}
    
    /*!
     * @abstract Singular delegate methods.
     * @discussion These delegate methods will be invoked when the user selects a single contact or property.
     */
- (void)contactPicker:(CNContactPickerViewController *)picker didSelectContact:(CNContact *)contact{
    NSMutableDictionary *kContact;
    kContact = [self convertToDictionaryFor:contact];
    
    [self respondToJS:kContact];
    
}
    
- (NSMutableDictionary *)convertToDictionaryFor:(CNContact *)contact{
    NSString *fullName, *email;
    fullName=[NSString stringWithFormat:@"%@%@",contact.familyName,contact.givenName];
    
    email=@"";
    
    
    NSMutableDictionary* phones = [NSMutableDictionary dictionaryWithCapacity:2];
    
    
    for (int i =0; i<contact.phoneNumbers.count; i++) {
        NSString *phoneNumber=[contact.phoneNumbers[i].value stringValue];
        
        [phones setObject:phoneNumber forKey:[NSString stringWithFormat:@"%d",i]];
    }
    
    NSMutableArray *addresses = [NSMutableArray array];
    
    NSString *tmpDirectory = NSTemporaryDirectory();
    NSString *fileName = [NSString stringWithFormat:@"%@_image.png", [fullName isEqualToString:@""] ? [NSDate date] : fullName];
    NSString *imagePath = [tmpDirectory stringByAppendingPathComponent:fileName];
    [contact.imageData writeToFile:imagePath atomically:YES];
    
    NSString *imageURL =[NSURL fileURLWithPath:imagePath].absoluteString;
    
    //NSLog(@"%@ %@", fullName, email);
    
    NSMutableDictionary* kContact = [NSMutableDictionary dictionaryWithCapacity:2];
    if (email) {
    }
    [kContact setObject:email forKey: @"email"];
    [kContact setObject:fullName forKey: @"displayName"];
    [kContact setObject:phones forKey:@"phones"];
    kContact[@"photoUrl"] = imageURL;
    kContact[@"address"] = addresses;
    
    
    [kContact setObject:@(1) forKey:@"id"];
    
    return kContact;
}
    @end
