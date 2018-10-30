using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using SchedullerAPI.Models;
using Newtonsoft.Json;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace SchedullerAPI.Controllers
{
    [Route("api/[controller]")]
    public class EventsController : Controller
    {
        private readonly SchedullerContext _context;

        public EventsController(SchedullerContext context) 
        {
            _context = context;
        }
        // GET: api/values
        [HttpGet]
        public IEnumerable<Event> Get()
        {
            return _context.Events.ToList();
        }

        // GET api/values/5
        [HttpGet("{id}")]
        public Event Get(int id)
        {
            return _context.Events.Where(e => e.Id == id).FirstOrDefault();
        }

        [HttpGet("{email}/{password}")]
        public ActionResult GetEvents(string email, string password)
        {
            try 
            {
                var user = _context.Users.Where(u => u.Email == email).FirstOrDefault();
                if (user.Password != password) 
                {
                    return StatusCode(401);
                }

                var events = _context.Events.Where(e => e.User == user).ToList();
                var eventsList = new List<object>();
                foreach (var evnt in events) 
                {
                    eventsList.Add(new
                    {
                        startDate = evnt.StartDate,
                        startTime = evnt.StartTime,
                        endDate = evnt.EndDate,
                        endTime = evnt.EndTime,
                        name = evnt.Name
                    });
                }

                var json = JsonConvert.SerializeObject(eventsList);

                return StatusCode(200, json);
            }
            catch (Exception exc) 
            {
                Console.WriteLine(exc.StackTrace);
                return StatusCode(500);
            }
        }

        // POST api/values
        [HttpPost("{email}/{password}")]
        public ActionResult Post([FromBody]Event value, string email, string password)
        {
            try 
            {
                var user = _context.Users.Where(u => u.Email == email).FirstOrDefault();
                if (user.Password != password) {
                    return StatusCode(401);
                }
                _context.Events.Add(value);
                value.User = user;
                _context.SaveChanges();

                var json = JsonConvert.SerializeObject(new
                {
                    startDate = value.StartDate,
                    startTime = value.StartTime,
                    endDate = value.EndDate,
                    endTime = value.EndTime,
                    name = value.Name
                });

                return StatusCode(200, json);
            }
            catch (Exception) 
            {
                return StatusCode(500);
            }
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public ActionResult Delete(int id)
        {
            try 
            {
                var deletedEvent = _context.Events.Where(e => e.Id == id).FirstOrDefault();
                var jsonResponse = JsonConvert.SerializeObject(new 
                {
                    id,
                    startDate = deletedEvent.StartDate,
                    startTime =deletedEvent.StartTime,
                    endDate = deletedEvent.EndDate,
                    endTime = deletedEvent.EndTime,
                    name = deletedEvent.Name     
                });
                _context.Events.Remove(deletedEvent);
                _context.SaveChanges();

                return StatusCode(200, jsonResponse);
            }
            catch 
            {
                return StatusCode(500);
            }
        }

        [HttpPost("{email}/{password}/delete")]
        public ActionResult DeleteEvent([FromBody] Event toDelete, string email, string password)
        {
            try 
            {
                var user = _context.Users.Where(u => u.Email == email).FirstOrDefault();
                if (user.Password != password)
                {
                    return StatusCode(401);
                }

                var deletedEvents = _context.Events.Where(evn => evn.Name == toDelete.Name
                                                         && evn.StartDate == toDelete.StartDate
                                                         && evn.EndDate == toDelete.EndDate
                                                         && evn.StartTime == toDelete.StartTime
                                                         && evn.EndTime == toDelete.EndTime).ToList();
                if (deletedEvents != null) 
                {
                    _context.Events.RemoveRange(deletedEvents);
                    _context.SaveChanges();
                    return StatusCode(200);
                }
                return StatusCode(404, toDelete);
            }
            catch (Exception)
            {
                return StatusCode(500);
            }
        }
    }
}
