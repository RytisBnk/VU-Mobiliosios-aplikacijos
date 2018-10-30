using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SchedullerAPI.Models;
using Newtonsoft.Json;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace SchedullerAPI.Controllers
{
    [Route("api/[controller]")]
    public class UsersController : Controller
    {
        private readonly SchedullerContext _context;

        public UsersController(SchedullerContext context) 
        {
            _context = context;
        }

        // GET: api/values
        [HttpGet]
        public IEnumerable<User> Get()
        {
            return _context.Users.ToList();
        }

        // GET api/values/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            var user = _context.Users.Where(u => u.Id == id)
                               .Include(u => u.Events)
                               .Include(u => u.Tasks)
                               .FirstOrDefault();

            var eventList = Enumerable.Empty<object>().Select(o => new
            {
                startDate = "",
                endDate = "",
                startTime = "",
                endTime = "",
                name = ""
            }).ToList();
            foreach (var evnt in user.Events) 
            {
                eventList.Add(new
                {
                    startDate = evnt.StartDate,
                    endDate = evnt.EndDate,
                    startTime = evnt.StartTime,
                    endTime = evnt.EndTime,
                    name = evnt.Name
                });
            }

            var taskList = Enumerable.Empty<object>().Select(o => new
            {
                startDate = "",
                endDate = "",
                duration = 0,
                name = "",
                priority = 0
            }).ToList();
            foreach (var task in user.Tasks)
            {
                taskList.Add(new
                {
                    startDate = task.StartDate,
                    endDate = task.EndDate,
                    duration = task.Duration,
                    name =task.Name,
                    priority = task.Priority
                });
            }

            return JsonConvert.SerializeObject(new
            {
                user.UserName,
                user.Email,
                Events = eventList,
                Tasks = taskList
            });
        }

        //get api/users/{email}
        [HttpGet("fetch/{email}/{password}")]
        public ActionResult GetUserInfo(string email, string password) 
        {
            var user = _context.Users.Where(u => u.Email == email)
                               .Include(u => u.Events)
                               .Include(u => u.Tasks)
                               .FirstOrDefault();

            if (user.Password == password) 
            {
                var eventList = Enumerable.Empty<object>().Select(o => new
                {
                    startDate = "",
                    endDate = "",
                    startTime = "",
                    endTime = "",
                    name = ""
                }).ToList();
                foreach (var evnt in user.Events)
                {
                    eventList.Add(new
                    {
                        startDate = evnt.StartDate,
                        endDate = evnt.EndDate,
                        startTime = evnt.StartTime,
                        endTime = evnt.EndTime,
                        name = evnt.Name
                    });
                }

                var taskList = Enumerable.Empty<object>().Select(o => new
                {
                    startDate = "",
                    endDate = "",
                    duration = 0,
                    name = "",
                    priority = 0
                }).ToList();
                foreach (var task in user.Tasks)
                {
                    taskList.Add(new
                    {
                        startDate = task.StartDate,
                        endDate = task.EndDate,
                        duration = task.Duration,
                        name = task.Name,
                        priority = task.Priority
                    });
                }

                var json = JsonConvert.SerializeObject(new
                {
                    userName = user.UserName,
                    email = user.Email,
                    events = eventList,
                    tasks = taskList
                });

                return StatusCode(200, json);
            }
            return StatusCode(401);
            
        }

        [HttpGet("login/{email}/{password}")]
        public ActionResult Login(string email, string password)
        {
            var user = _context.Users.Where(u => u.Email == email).FirstOrDefault();

            if (user.Password == password)
            {
                var json = JsonConvert.SerializeObject(new
                {
                    userName = user.UserName,
                    email = user.Email,
                    password = user.Password
                });

                return StatusCode(200, json);
            }
            return StatusCode(401);
        }

        // POST api/values
        [HttpPost]
        public User Post([FromBody] User value)
        {
            try 
            {
                _context.Users.Add(value);
                _context.SaveChanges();

                return value;
            }
            catch (Exception)
            {
                return null;
            }
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public ActionResult Delete(int id)
        {
            try 
            {
                var user = _context.Users.Where(u => u.Id == id)
                               .Include(u => u.Events)
                               .Include(u => u.Tasks)
                               .FirstOrDefault();
                var jsonResponse = JsonConvert.SerializeObject(new
                {
                    id,
                    userName = user.UserName,
                    email = user.Email,
                });
                var events = user.Events;
                var tasks = user.Tasks;
                foreach (var evnt in events)
                {
                    _context.Events.Remove(evnt);
                }
                foreach (var task in tasks)
                {
                    _context.Tasks.Remove(task);
                }

                _context.Users.Remove(user);
                _context.SaveChanges();

                return StatusCode(200, jsonResponse);
            }
            catch (Exception) 
            {
                return StatusCode(500, "The requested resource doesn't exist");
            }
        }
    }
}
