using Microsoft.EntityFrameworkCore.Migrations;

namespace SchedullerAPI.Migrations
{
    public partial class FixSpelling : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "StarTime",
                table: "Events",
                newName: "StartTime");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "StartTime",
                table: "Events",
                newName: "StarTime");
        }
    }
}
