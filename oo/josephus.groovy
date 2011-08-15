    class Chain
    {
        def size
        def first

        def init(siz)
        {
            def last
            size = siz
            for(def i = 0 ; i < siz ; i++)
            {
                def current = new Person()
                current.count = i+1
                if (i == 0) first = current
                if (last != null)
                {
                    last.next = current
                }
                current.prev = last
                last = current
            }
            first.prev = last
            last.next = first
        }

        def kill(nth)
        {
            def current = first
            def shout = 1
            while(current.next != current)
            {
                shout = current.shout(shout,nth)
                current = current.next
            }
            first = current
        }
    }

    class Person
    {
        def count
        def prev
        def next

        def shout(shout,nth) {
            if (shout == 1) {
                prev.next = next
                next.prev = prev
                return 2
            }
            if (shout == nth) {
                return 1
            } else {
                return shout + 1
            }
        }
    }

    def main(args)
    {
        println "Starting"
        def ITER = 100000
        def start = System.nanoTime()
        for(def i = 0 ; i < ITER ; i++)
        {
            def chain = new Chain()
            chain.init(40)
            chain.kill(3)
        }
        def end = System.nanoTime()
        println "Total time = " + ((end - start)/(ITER * 1000)) + " microseconds"
    }

    def chain = new Chain()
    chain.init(40)
    chain.kill(3)
    println(chain.first.count)

    def ITER = 100000
    for(def i = 0 ; i < ITER ; i++) {
        chain = new Chain()
        chain.init(40)
        chain.kill(3)
    }
    def start = System.nanoTime()
    for(def i = 0 ; i < ITER ; i++) {
        chain = new Chain()
        chain.init(40)
        chain.kill(3)
    }
    def end = System.nanoTime()
    println "Time per iteration = " + ((end - start)/(ITER * 1000)) + " microseconds"
