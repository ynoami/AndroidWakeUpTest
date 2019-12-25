using System;
using System.Net.Sockets;
using System.Threading;
using System.Net;
using System.Threading.Tasks;

namespace RequesterConsole
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");


            var listen = new TcpListener(IPAddress.Parse("127.0.0.1"), __port);
            listen.Start();


            while (true)
            {
                Thread.Sleep(1000);
            }
        }

        private async void Hoge(TcpListener listener)
        {
            while (true)
            {
                var client = listener.AcceptTcpClientAsync()
                    .ContinueWith(t =>
                    {
                        Hoge(listener);
                        var stream = t.Result.GetStream();
                        while (true)
                        {
                            var readed = await stream.ReadAsync();
                        }
                    });
            }
        }

        private static int __port = 9080;
    }
}
