using System;
namespace SchedullerAPI.Models
{
    public class Event
    {
        public long Id { get; set; }
        public string StartDate { get; set; }
        public string StartTime { get; set; }
        public string EndDate { get; set; }
        public string EndTime { get; set; }
        public string Name { get; set; }

        public virtual User User { get; set; }
    }
}
