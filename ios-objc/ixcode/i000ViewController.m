#import "AppDelegate.h"
#import "i000ViewController.h"
#include <sys/time.h>

@interface i000ViewController ()

@end

@implementation i000ViewController

@synthesize OUTPUT;

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    // Do any additional setup after loading the view, typically from a nib.
    NSLog(@"addObserver");
    

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(response_received:)
        name:globalConn.responseReceivedNotification object:nil];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    NSLog(@"removeObserver");


    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)inputAction:(id)sender {
    NSMutableDictionary* data = [[NSMutableDictionary alloc] init];
    
    [data setObject:@"test" forKey:@"obj"];
    [data setObject:@"input1" forKey:@"act"];
    [data setObject:@"click" forKey:@"data"];
    

    [self mock_capture_input:data];
}

- (void)response_received:(NSNotification *) notification {

    if (![[notification name] isEqualToString:@"response"]) return;
    
    NSLog(@"notification %@, thread %@, response: %@:%@ uerr:%@ derr:%@ ustr:%@",
        [notification name],
        [NSThread currentThread],
        [globalConn.response s:@"obj"],
        [globalConn.response s:@"act"],
        [globalConn.response s:@"uerr"],
        [globalConn.response s:@"derr"],
        [globalConn.response s:@"ustr"]);
    // {"obj":"associate","act":"mock","to_login_name":"IXCODE_ACCOUNT","data":{"obj":"test","act":"OUTPUT1","data":"blah"}}
    if ([[globalConn.response s:@"obj"] isEqualToString:@"test"]) {
        if ([[globalConn.response s:@"act"] isEqualToString:@"output1"]) {
            //OUTPUT.text = [[[globalConn.response optJSONArray:@"data"] optJSONObject:1] optString:@"show" defaultValue:@"none"];
            //OUTPUT.text = [[globalConn.response optJSONArray:@"data"] optString:1];
            //OUTPUT.text = [globalConn.response optString:@"data"];
            

            

            //OUTPUT.text = [[[globalConn.response a:@"data"] o:1] s:@"show" d:@"none"];
            //OUTPUT.text = [[globalConn.response a:@"data"] s:1];
            OUTPUT.text = [globalConn.response s:@"data"];
        }
    }
    if ([[globalConn.response s:@"obj"] isEqualToString:@"associate"]) {
        if ([[globalConn.response s:@"act"] isEqualToString:@"mock"]) {
            struct timeval time;
            gettimeofday(&time, NULL);
            long unsigned millis = (time.tv_sec*1000) + (time.tv_usec/1000);
            OUTPUT.text = [NSString stringWithFormat:@"mock resp %lu", millis];
        }
    }
        
    if ([[globalConn.response s:@"obj"] isEqualToString:@"person"]) {
        if ([[globalConn.response s:@"act"] isEqualToString:@"login"]) {
            if ([[globalConn.response s:@"ustr"] isEqualToString:@""]) {
                OUTPUT.text = [NSString stringWithFormat:@"account ID %@ log in OK\nPlease log in on Project Toolbox with ID: %@ password: 1", IXCODE_ACCOUNT, TOOLBOX_ACCOUNT];
                }
        }
    }
    
    return;
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


-(void)mock_capture_input:(NSMutableDictionary*)data {
    NSMutableDictionary* req = [[NSMutableDictionary alloc] init];
    
    [req setObject:@"associate" forKey:@"obj"];
    [req setObject:@"mock" forKey:@"act"];
    [req setObject:TOOLBOX_ACCOUNT forKey:@"to_login_name"];
    [req setObject:data forKey:@"data"];
    
    [globalConn send:req];
}

@end