using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using SchedullerAPI.Models;
using Newtonsoft.Json;
using Task = SchedullerAPI.Models.Task;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace SchedullerAPI.Controllers
{
    [Route("api/[controller]")]
    public class TasksController : Controller
    {
        private readonly SchedullerContext _context;

        public TasksController(SchedullerContext context)
        {
            _context = context;
        }

        //get api/tasks
        [HttpGet]
        public IEnumerable<Models.Task> GetTasks()
        {
            return _context.Tasks.ToList();
        }

        // get api/tasks/{id}
        [HttpGet("{id}")]
        public Models.Task GetTask(int id) {
            return _context.Tasks.Where(task => task.Id == id).FirstOrDefault();
        }

        [HttpGet("{email}/{password}")]
        public ActionResult GetTasks(string email, string password)
        {
            try 
            {
                var user = _context.Users.Where(u => u.Email == email).FirstOrDefault();
                if (user.Password != password)
                {
                    return StatusCode(401);
                }

                var tasks = _context.Tasks.Where(t => t.User == user).ToList();
                var taskList = new List<object>();
                foreach (var task in tasks) 
                {
                    taskList.Add(new
                    {
                        startDate = task.StartDate,
                        endDate = task.EndDate,
                        name = task.Name,
                        duration = task.Duration,
                        priority = task.Priority
                    });
                }

                var json = JsonConvert.SerializeObject(taskList);

                return StatusCode(200, json);
            }
            catch 
            {
                return StatusCode(500);
            }
        }

        //post api/tasks
        [HttpPost("{email}/{password}")]
        public ActionResult CreateTask([FromBody] Models.Task task, string email, string password) 
        {
            try {
                var user = _context.Users.Where(u => u.Email == email).FirstOrDefault();
                if (user.Password != password) {
                    return StatusCode(401);
                }

                _context.Tasks.Add(task);
                task.User = user;
                _context.SaveChanges();

                var json = JsonConvert.SerializeObject(new
                {
                    startDate =task.StartDate,
                    endDate = task.EndDate,
                    name = task.Name,
                    duration = task.Duration,
                    priority = task.Priority
                });

                return StatusCode(200, json);
            }
            catch (Exception) 
            {
                return StatusCode(500);
            }
        }

        [HttpDelete("{id}")]
        public ActionResult DeleteTask(int id) 
        {
            try 
            {
                var deletedTask = _context.Tasks.Where(t => t.Id == id).FirstOrDefault();
                var jsonResponse = JsonConvert.SerializeObject(new
                {
                    id,
                    startDate = deletedTask.StartDate,
                    endDate = deletedTask.EndDate,
                    duration = deletedTask.Duration,
                    name = deletedTask.Name,
                    priority = deletedTask.Priority
                });

                _context.Tasks.Remove(deletedTask);
                _context.SaveChanges();

                return StatusCode(200, jsonResponse);
            }
            catch (Exception)
            {
                return StatusCode(500);
            }
        }

        [HttpPost("{email}/{password}/delete")]
        public ActionResult Delete([FromBody] Models.Task task, string email, string password)
        {
            try 
            {
                var user = _context.Users.Where(u => u.Email == email).FirstOrDefault();
                if (user.Password != password)
                {
                    return StatusCode(401);
                }

                var tasks = _context.Tasks.Where(t => t.Name == task.Name
                                                 && t.StartDate == task.StartDate
                                                 && t.EndDate == task.EndDate
                                                 && t.Duration == task.Duration
                                                 && t.Priority == task.Priority).ToList();
                if (tasks != null) 
                {
                    _context.Tasks.RemoveRange(tasks);
                    _context.SaveChanges();
                    return StatusCode(200);
                }
                return StatusCode(400);
            }
            catch 
            {
                return StatusCode(500);
            }
        }
    }
}
