#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import <AddressBookUI/AddressBookUI.h>
#import <Cordova/CDVPlugin.h>
#import <Contacts/Contacts.h>
#import <ContactsUI/ContactsUI.h>
@interface ContactPicker : CDVPlugin <ABPersonViewControllerDelegate>

@property(strong) NSString* callbackID;

- (void) chooseContact:(CDVInvokedUrlCommand*)command;

- (void) addContact:(CDVInvokedUrlCommand*)command;

@end
