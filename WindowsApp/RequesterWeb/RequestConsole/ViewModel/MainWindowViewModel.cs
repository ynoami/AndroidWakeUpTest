using Prism.Mvvm;
using System;
using System.Collections.Generic;
using System.Text;
using System.ComponentModel;
using System.Reactive.Linq;
using Reactive.Bindings;
using System.Net;
using System.Net.Sockets;
using RequestConsole.Model;

namespace RequestConsole.ViewModel
{
    class MainWindowViewModel : BindableBase
    {
        public MainWindowViewModel()
        {
            SendCommand.Subscribe(_ =>
            {
                MySetting.Instance.ServerIPAddress = ServerIPAddress.Value;

                using (var client = new TcpClient(ServerIPAddress.Value, __controllPort))
                {
                    var stream = client.GetStream();
                    var message = Encoding.ASCII.GetBytes((__counter++).ToString());
                    stream.Write(message);
                }
            });

            ServerIPAddress.Value = MySetting.Instance.ServerIPAddress;
        }

        public ReactiveCommand SendCommand { get; set; } = new ReactiveCommand();

        public ReactiveProperty<string> ServerIPAddress { get; set; } = new ReactiveProperty<string>();

        private static int __controllPort = 9081;

        private static int __counter = 0;
    }
}
