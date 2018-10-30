using System;
using Microsoft.EntityFrameworkCore;

namespace SchedullerAPI.Models
{
    public class SchedullerContext : DbContext
    {
        public SchedullerContext(DbContextOptions<SchedullerContext> options) : base(options)
        {
        }

        public DbSet<Task> Tasks { get; set; }
        public DbSet<Event> Events { get; set; }
        public DbSet<User> Users { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<User>()
                        .HasIndex(u => u.Email)
                        .IsUnique();
        }
    }
}
