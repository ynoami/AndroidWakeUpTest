using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;

namespace RequesterServer
{
    class Program
    {
        static void Main(string[] args)
        {
            var ipAddress = IPAddress.Parse("127.0.0.1");

            // コントロールポートのリッスン開始
            var controllListener = new TcpListener(IPAddress.Parse("127.0.0.1"), __controllPort);
            controllListener.Start();
            ListenControlSocket(controllListener);

            // ブロードキャストポートのリッスン開始
            var broadCastListener = new TcpListener(IPAddress.Parse("127.0.0.1"), __broadCastPort);
            broadCastListener.Start();
            ListenBroadCastSocket(broadCastListener);

            // 無限待ち！
            while (true)
            {
                Thread.Sleep(1000);
            }
        }

        private static void ListenBroadCastSocket(TcpListener listener)
        {
            listener.AcceptTcpClientAsync()
                .ContinueWith(async t =>
                {
                    // 次の待ち受けを直ぐに開始する
                    ListenBroadCastSocket(listener);

                    //
                    var buffer = new byte[1024];
                    var stream = t.Result.GetStream();
                    lock(_streamList)
                    {
                        _streamList.Add(stream);
                    }
                    while (true)
                    {
                        var readedLength = await stream.ReadAsync(buffer, 0, buffer.Length);

                        Console.WriteLine($"データ受信[ListenBroadCastSocket] : {readedLength}");

                        // 0バイト受信の場合はクローズされているはず
                        if (readedLength <= 0)
                        {
                            break;
                        }
                    }
                });
        }

        private static void ListenControlSocket(TcpListener listener)
        {
            listener.AcceptTcpClientAsync()
                .ContinueWith(async t =>
                {
                    // 次の待ち受けを直ぐに開始する
                    ListenControlSocket(listener);

                    // 
                    var buffer = new byte[1024];
                    var stream = t.Result.GetStream();
                    while (true)
                    {
                        var readedLength = await stream.ReadAsync(buffer, 0, buffer.Length);

                        Console.WriteLine($"データ受信[ListenControlSocket] : {readedLength}");

                        // 0バイト受信の場合はクローズされているはず
                        if (readedLength <= 0)
                        {
                            break;
                        }

                        lock (_streamList)
                        {
                            foreach (var targetStream in _streamList)
                            {
                                targetStream.WriteAsync(buffer, 0, readedLength);
                            }
                        }
                    }
                });
        }

        private static List<NetworkStream> _streamList = new List<NetworkStream>();

        private static int __broadCastPort = 9082;
        private static int __controllPort = 9081;
    }
}
