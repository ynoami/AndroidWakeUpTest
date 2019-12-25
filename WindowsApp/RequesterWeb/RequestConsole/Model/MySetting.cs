using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace RequestConsole.Model
{
    class MySetting
    {
        public static MySetting Instance;
        private const string SettingFileName = "mysetting.json";

        static MySetting()
        {
            if (File.Exists(SettingFileName))
            {
                try
                {
                    Instance = JsonConvert.DeserializeObject<MySetting>(File.ReadAllText(SettingFileName, Encoding.UTF8));
                }
                catch(Exception)
                {
                }
            }

            Instance = Instance ?? new MySetting() { ServerIPAddress = "127.0.0.1" };
        }

        public void Save()
        {
            File.WriteAllText(SettingFileName, JsonConvert.SerializeObject(Instance));
        }

        public string ServerIPAddress { get; set; }
    }
}
