using System;
namespace SchedullerAPI.Models
{
    public class Task
    {
        public long Id { get; set; }
        public string StartDate { get; set; }
        public string EndDate { get; set; }
        public int Duration { get; set; }
        public int Priority { get; set; }
        public string Name { get; set; }

        public virtual User User { get; set; }
    }
}
