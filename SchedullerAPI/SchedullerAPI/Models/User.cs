using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace SchedullerAPI.Models
{
    public class User
    {
        public long Id { get; set; }
        public string UserName { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }

        public virtual ICollection<Task> Tasks { get; set; }
        public virtual ICollection<Event> Events { get; set; }
    }
}
