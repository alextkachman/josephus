import collection.mutable.ArrayBuffer

/* 
 * Improvements by Rahul Goma Phulore http://twitter.com/missingfaktor http://stackoverflow.com/users/192247/missingfaktor
 *  - Changed for loop to ExtraControls.loop
 *  - Used ArrayBuffer instead of Linked List
 */

object ExtraControls {
  def loop(a: Int, b: Int)(f: Int => Unit): Unit = {
    var i = a
    while(i <= b) {
      f(i)
      i += 1
    }
  }
}

class Person(val count: Int, var prev: Person = null, var next: Person= null) {
  def shout(counter: Int, nth: Int) = {
    if (counter == 1) {
      prev.next = next
      next.prev = prev
    }
    if (counter == nth) 1 else counter + 1
  }
}

class Chain(val count: Int) {
  import ExtraControls.loop

  private var last: Person = null
  private var first: Person = null
  loop(1, count) { i =>
    val p = new Person(i,last)
    if (i == 1) first = p
    else last.next = p
    last = p
  }
  first.prev = last
  last.next = first
  last = null // can be forgotten
  def shout(nth: Int): Int = {
    var counter = 1
    var current = first
    while (current != current.next) {
      counter = current.shout(counter,nth)
      current = current.next
    }
    current.count
  }
}
object Josephus{
  import ExtraControls.loop

  def timeit(tag: String)(f: => Unit): Unit = {
    val iters = 1000000
    var start = System.currentTimeMillis()
    loop(1, iters) { _ =>
        f
    }
    var end = System.currentTimeMillis()
    println((end - start) * 1000.0 / iters + " microseconds ( " + tag + " )")
  }

  def warmup(f: => Unit): Unit = {
    loop(1, 100000) { _ =>
        f
    }
  }

  def main(args : Array[String]) : Unit = {
    val chain = new Chain(40)
    println(chain.shout(3))

    warmup {
      val chain = new Chain(40)
      chain.shout(3)
    }
    timeit("oo") {
      val chain = new Chain(40)
      chain.shout(3)
    }

    println(shoutPatternMatch(40, 3))
    warmup {
      shoutPatternMatch(40,3)
    }
    timeit ("element recursive") {
      shoutPatternMatch(40,3)
    }

    println(shoutListProcessing(40,3))
    warmup {
      shoutListProcessing(40,3)
    }
    timeit ("list reduction"){
      shoutListProcessing(40,3)
    }
  }

  def shoutListProcessing(soldiers: ArrayBuffer[Int], n: Int): Int = {
    var counter = 0
    var people = soldiers
    while (people.length > 1) {
        val newPeople = ArrayBuffer.empty[Int]
        loop(0, people.length - 1) { i =>
          if ((counter + i) % n != 0) {
            newPeople += people(i)
          }
        }
        counter = (counter + people.length) % n
        people = newPeople
    }
    people(0)
  }
  
  def shoutListProcessing(count: Int, nth: Int): Int = {
    shoutListProcessing(ArrayBuffer.range(1, 41),3)
  }

  def shoutPatternMatch(soldiers : List[Int], survivors: List[Int], n: Int, counter: Int): Int = {
    (soldiers, survivors, n, counter) match {
      case(h :: Nil, Nil, _, _) => h
      case(Nil, s, _, _) => shoutPatternMatch(s reverse, Nil, n, counter)
      case(h :: t, s, n, 1) =>
        shoutPatternMatch(t, s, n, 2)
      case(h :: t, s, n, c) =>
        shoutPatternMatch(t, h :: s, n, if(c == n) 1 else c + 1)
    }
  }

  def shoutPatternMatch(count: Int, nth: Int): Int = {
    shoutPatternMatch(List.range(1, 41),Nil,3,1)
  }

}

