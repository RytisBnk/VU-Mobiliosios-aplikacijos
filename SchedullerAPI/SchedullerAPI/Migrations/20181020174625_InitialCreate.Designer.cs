﻿// <auto-generated />
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using SchedullerAPI.Models;

namespace SchedullerAPI.Migrations
{
    [DbContext(typeof(SchedullerContext))]
    [Migration("20181020174625_InitialCreate")]
    partial class InitialCreate
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "2.1.4-rtm-31024")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("SchedullerAPI.Models.Event", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("EndDate");

                    b.Property<string>("EndTime");

                    b.Property<string>("Name");

                    b.Property<string>("StarTime");

                    b.Property<string>("StartDate");

                    b.HasKey("Id");

                    b.ToTable("Events");
                });

            modelBuilder.Entity("SchedullerAPI.Models.Task", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("Duration");

                    b.Property<string>("EndDate");

                    b.Property<string>("Name");

                    b.Property<int>("Priority");

                    b.Property<string>("StartDate");

                    b.HasKey("Id");

                    b.ToTable("Tasks");
                });
#pragma warning restore 612, 618
        }
    }
}
